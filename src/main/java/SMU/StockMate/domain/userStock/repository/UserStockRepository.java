package SMU.StockMate.domain.userStock.repository;

import SMU.StockMate.domain.userStock.entity.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    Optional<UserStock> findByUserIdAndStockCode(Long userId, String stockCode);
    List<UserStock> findByUserId(Long userId);
}
