package SMU.StockMate.domain.stock.command.repository;

import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCommandRepository extends JpaRepository<Stock, Long> {

}
