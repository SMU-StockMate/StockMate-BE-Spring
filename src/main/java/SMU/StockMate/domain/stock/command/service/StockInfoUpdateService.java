package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.entity.Stock;

import java.time.LocalDate;
import java.util.List;

public interface StockInfoUpdateService {
    int refresh(List<Stock> stocks);
}
