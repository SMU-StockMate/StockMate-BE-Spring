package SMU.StockMate.domain.stock.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StockInfoResponse {
    private List<StocksInfoDto> stockInfos;
    private String nextCursor;
}
