package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.entity.Stock;

import java.util.List;

public interface StockInfoUpdateService {
    void refresh(List<Stock> stocks);
}
