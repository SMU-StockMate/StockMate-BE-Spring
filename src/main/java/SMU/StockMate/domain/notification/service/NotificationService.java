package SMU.StockMate.domain.notification.service;


import SMU.StockMate.domain.notification.dto.PriceAlertRequestDTO;

public interface NotificationService {
    void sendNotificationToAllTokens(Long userId, String title, String body);
    void registerPriceAlert(PriceAlertRequestDTO dto);
    void checkAndSendPriceAlerts();
    void isRead(Long id);
}
