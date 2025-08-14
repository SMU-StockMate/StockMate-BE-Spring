package SMU.StockMate.domain.dailyStock.repository;

import SMU.StockMate.domain.dailyStock.entity.DailyStock;

import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface DailyStockRepository extends JpaRepository<DailyStock, Long> {
    List<DailyStock> findByStockAndTradeDateBetweenOrderByTradeDateAsc(Stock stock, LocalDate from, LocalDate to);
}
