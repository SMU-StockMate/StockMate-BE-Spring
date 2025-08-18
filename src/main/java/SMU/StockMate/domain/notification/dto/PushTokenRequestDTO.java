package SMU.StockMate.domain.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushTokenRequestDTO {
    private Long userId;
    private String deviceId;
    private String token;
}
