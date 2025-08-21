package SMU.StockMate.domain.post.repository;

import SMU.StockMate.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByStockId(Long stockId, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.id = :stockId " +
            "AND  p.id > :cursor")
    List<Post> findAllByStockIdWithCursor(@Param("stockId") Long stockId,
                                          @Param("cursor") Long cursor,
                                          Pageable pageable);

}
