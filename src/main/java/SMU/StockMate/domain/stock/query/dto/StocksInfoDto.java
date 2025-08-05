package SMU.StockMate.domain.stock.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StocksInfoDto {
    private String stockCode;
    private String koreanName;
    private int basePrice;
}
