package SMU.StockMate.domain.stock.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StockSearchListResponse {
    private List<StocksInfoDto> stockInfos;
    private String nextCursor;
}
