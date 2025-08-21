package SMU.StockMate.domain.stock.service.command;

import SMU.StockMate.domain.stock.entity.Stock;

import java.util.List;

public interface StockUpdateService {
    int refresh(List<Stock> stocks);
}
