package SMU.StockMate.domain.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceAlertRequestDTO {
    private Long userId;
    private String stockCode;
    private Long targetPrice;
}
