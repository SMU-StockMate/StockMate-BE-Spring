package SMU.StockMate.domain.stock.repository;

import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s " +
            "WHERE s.isActivate = true " +
            "AND s.stockCode LIKE %:keyword% or s.koreanName LIKE %:keyword% ")
    List<Stock> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Stock s " +
            "WHERE s.isActivate = true " +
            "AND (s.stockCode LIKE %:keyword% OR s.koreanName LIKE %:keyword%) " +
            "AND s.id > :cursor ")
    List<Stock> findByNameContainingNextPage(@Param("cursor") Long cursor,
                                             @Param("keyword") String keyword,
                                             Pageable pageable);

    Optional<Stock> findByStockCode(String stockCode);
}
