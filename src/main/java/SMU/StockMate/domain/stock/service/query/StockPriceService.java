package SMU.StockMate.domain.stock.service.query;

import SMU.StockMate.domain.stock.dto.StockPriceResponseDTO;

public interface StockPriceService {
    StockPriceResponseDTO getPrice(String stockCode);
}
