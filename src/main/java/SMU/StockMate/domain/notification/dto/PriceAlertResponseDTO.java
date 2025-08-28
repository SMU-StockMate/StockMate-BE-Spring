package SMU.StockMate.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PriceAlertResponseDTO {

    private Long id;
    private Long userId;
    private String stockCode;
    private Long targetPrice;
    private boolean triggered;
}
