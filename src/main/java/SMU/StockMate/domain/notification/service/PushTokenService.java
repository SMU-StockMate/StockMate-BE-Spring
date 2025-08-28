package SMU.StockMate.domain.notification.service;
import SMU.StockMate.domain.notification.dto.PushTokenRequestDTO;

public interface PushTokenService {
    void registerToken(PushTokenRequestDTO dto);
}