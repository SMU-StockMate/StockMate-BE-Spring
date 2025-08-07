package SMU.StockMate.domain.stock.command.repository;

import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.enums.StockUpdateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StockUpdateLogRepository extends JpaRepository<StockUpdateLog, Long> {
    boolean existsByUpdateDateAndStatus(LocalDate updateDate, StockUpdateStatus status);
}
