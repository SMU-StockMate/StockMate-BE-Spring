package SMU.StockMate.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NotificationRequestDTO {
    private Long userId;
    private String targetToken;
    private String title;
    private String body;

}
