package SMU.StockMate.domain.stock.query.service;

import SMU.StockMate.domain.stock.query.dto.StockPriceResponseDTO;

public interface StockPriceService {
    StockPriceResponseDTO getPrice(String stockCode);
}
