package SMU.StockMate.domain.dailyStock.service;

import SMU.StockMate.domain.dailyStock.dto.DailyStockResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DailyStockService {
    List<DailyStockResponseDTO> getDailyStock(String stockCode, LocalDate from, LocalDate to);

    List<DailyStockResponseDTO> getLast30Days(String stockCode);
}