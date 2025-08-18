package SMU.StockMate.domain.notification.repository;

import SMU.StockMate.domain.notification.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {


    List<PriceAlert> findByTriggeredFalse();


}
