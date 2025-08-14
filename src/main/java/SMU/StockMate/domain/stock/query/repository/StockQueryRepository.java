package SMU.StockMate.domain.stock.query.repository;

import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockQueryRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s " +
            "WHERE DATE(s.updatedAt) = :date " +
            "AND s.stockCode LIKE %:keyword% or s.koreanName LIKE %:keyword% ")
    List<Stock> findByNameContaining(@Param("date")LocalDate date, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Stock s " +
            "WHERE DATE(s.updatedAt) = :date " +
            "AND (s.stockCode LIKE %:keyword% OR s.koreanName LIKE %:keyword%) " +
            "AND s.id > :cursor ")
    List<Stock> findByNameContainingNextPage(@Param("cursor") Long cursor, @Param("date") LocalDate date,
                                             @Param("keyword") String keyword, Pageable pageable);
    
  Optional<Stock> findByStockCode(String stockCode);
  
}
