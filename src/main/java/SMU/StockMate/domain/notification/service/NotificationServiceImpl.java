package SMU.StockMate.domain.notification.service;

import SMU.StockMate.domain.notification.dto.PriceAlertRequestDTO;
import SMU.StockMate.domain.notification.entity.Notification;
import SMU.StockMate.domain.notification.entity.PriceAlert;
import SMU.StockMate.domain.notification.entity.PushToken;
import SMU.StockMate.domain.notification.repository.NotificationRepository;
import SMU.StockMate.domain.notification.repository.PriceAlertRepository;
import SMU.StockMate.domain.notification.repository.PushTokenRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.dto.StockPriceResponseDTO;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.stock.service.query.StockPriceService;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final FcmService fcmService;
    private final StockPriceService stockPriceService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PriceAlertRepository priceAlertRepository;
    private final StockRepository stockRepository;
    private final PushTokenRepository pushTokenRepository;

    private Notification createNotification(Long userId, String title, String body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .body(body)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }


    private void sendPushMessage(String targetToken, String title, String body) {
        try {
            fcmService.sendMessageTo(targetToken, title, body);
        } catch (IOException e) {
            throw new RuntimeException("FCM 전송 실패", e);
        }
    }


    public void sendNotificationToAllTokens(Long userId, String title, String body) {
        Notification notification = createNotification(userId, title, body);

        User user = notification.getUser();
        List<PushToken> tokens = pushTokenRepository.findAllByUserAndActiveTrue(user);

        for (PushToken token : tokens) {
            sendPushMessage(token.getToken(), title, body);
        }
    }

    @Transactional
    public void isRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림이 존재하지 않습니다."));
        notification.setIsRead(true);
    }

    @Transactional
    public void registerPriceAlert(PriceAlertRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자."));

        PriceAlert alert = PriceAlert.builder()
                .user(user)
                .stockCode(dto.getStockCode())
                .targetPrice(dto.getTargetPrice())
                .triggered(false)
                .build();

        priceAlertRepository.save(alert);
    }

    @Transactional
    public void checkAndSendPriceAlerts() {
        List<PriceAlert> alerts = priceAlertRepository.findByTriggeredFalse();

        for (PriceAlert alert : alerts) {
            Stock stock = stockRepository.findByStockCode(alert.getStockCode())
                    .orElseThrow(() -> new IllegalArgumentException("알 수 없는 종목 코드: " + alert.getStockCode()));

            StockPriceResponseDTO price = stockPriceService.getPrice(stock.getStockCode());

            if (price.getPrice() >= alert.getTargetPrice()) {
                User user = alert.getUser();
                String title = stock.getKoreanName() + " 목표가 도달";
                String body = "현재가 " + price.getPrice() + "원이 목표가를 넘었습니다!";

                Notification notification = createNotification(user.getId(), title, body);

                List<PushToken> tokens = pushTokenRepository.findAllByUserAndActiveTrue(user);
                for (PushToken token : tokens) {
                    sendPushMessage(token.getToken(), title, body);
                }

                alert.setTriggered(true);
            }
        }
    }
}
