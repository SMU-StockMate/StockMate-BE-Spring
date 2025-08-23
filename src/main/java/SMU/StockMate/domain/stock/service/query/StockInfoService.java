package SMU.StockMate.domain.stock.service.query;

import SMU.StockMate.domain.stock.dto.StockDetailResponse;
import SMU.StockMate.domain.stock.dto.StockSearchListResponse;

public interface StockInfoService {
    StockSearchListResponse search(String keyword, String cursor);

    StockDetailResponse getStockDetail(String stockCode);
}
