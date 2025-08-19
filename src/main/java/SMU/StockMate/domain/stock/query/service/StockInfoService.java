package SMU.StockMate.domain.stock.query.service;

import SMU.StockMate.domain.stock.query.dto.StockDetailResponse;
import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;

public interface StockInfoService {
    StockInfoResponse search(String keyword, String cursor);

    StockDetailResponse getStockDetail(String stockCode);
}
