package SMU.StockMate.domain.stock.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPriceResponseDTO {
    private String symbol;
    private String name;
    private long price;
    private long change;
    private double changePercent;
    private long volume;
    private OffsetDateTime timestamp;


}
