package SMU.StockMate.domain.stock.command.repository;

import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
