package SMU.StockMate.domain.stock.query.service;

import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;

import java.util.List;

public interface StockSearchService {
    StockInfoResponse search(String keyword, String cursor);
}
