package SMU.StockMate.domain.notification.service;

import SMU.StockMate.domain.notification.dto.PriceAlertRequestDTO;
import SMU.StockMate.domain.notification.entity.Notification;
import SMU.StockMate.domain.notification.entity.PriceAlert;
import SMU.StockMate.domain.notification.entity.PushToken;
import SMU.StockMate.domain.notification.repository.NotificationRepository;
import SMU.StockMate.domain.notification.repository.PriceAlertRepository;
import SMU.StockMate.domain.notification.repository.PushTokenRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.query.dto.StockPriceResponseDTO;
import SMU.StockMate.domain.stock.query.repository.StockQueryRepository;
import SMU.StockMate.domain.stock.query.service.StockPriceService;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final FcmService fcmService;
    private final StockPriceService stockPriceService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PriceAlertRepository priceAlertRepository;
    private final StockQueryRepository stockQueryRepository;
    private final PushTokenRepository pushTokenRepository;

    public void sendNotificationToAllTokens(Long userId, String title, String body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        List<PushToken> tokens = pushTokenRepository.findAllByUserAndActiveTrue(user);

        for (PushToken token : tokens) {
            sendNotification(user.getId(), token.getToken(), title, body);
        }
    }


    private void sendNotification(Long userId, String targetToken, String title, String body) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

            Notification notification = Notification.builder()
                    .user(user)
                    .title(title)
                    .body(body)
                    .isRead(false)
                    .build();

            notificationRepository.save(notification);

            fcmService.sendMessageTo(targetToken, title, body);

        } catch (IOException e) {
            throw new RuntimeException("FCM 전송 실패", e);
        }
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
            Stock stock = stockQueryRepository.findByStockCode(alert.getStockCode())
                    .orElseThrow(() -> new IllegalArgumentException("알 수 없는 종목 코드: " + alert.getStockCode()));

            StockPriceResponseDTO price = stockPriceService.getPrice(stock.getStockCode());

            if (price.getPrice() >= alert.getTargetPrice()) { // 현재가가 목표가격보다 같거나 클 때 알림을 보냄
                User user = alert.getUser();
                List<PushToken> tokens = pushTokenRepository.findAllByUserAndActiveTrue(user);
                for (PushToken token : tokens) {
                    sendNotification(
                            user.getId(),
                            token.getToken(),
                            stock.getKoreanName() + " 목표가 도달",
                            "현재가 " + price.getPrice() + "원이 목표가를 넘었습니다!"
                    );
                }

                alert.setTriggered(true);
            }
        }
    }
}
