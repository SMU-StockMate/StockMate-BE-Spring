package SMU.StockMate.domain.leaderBoard.repository;

import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardDTO;
import SMU.StockMate.domain.leaderBoard.entity.LeaderBoard;
import SMU.StockMate.domain.leaderBoard.entity.LeaderBoardCandidate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Long> {
    // JPQL을 사용한 특정 날짜 상위 100
    @Query("""
        SELECT new SMU.StockMate.domain.leaderBoard.dto.LeaderBoardDTO(
           ld.userId, ld.rank, ld.nickname, ld.totalReturns, ld.totalAsset
      )
      FROM LeaderBoard ld
      WHERE ld.date = :date
      ORDER BY ld.rank ASC
      """)
    List<LeaderBoardDTO> findTop100ByDate(@Param("date") LocalDate date, Pageable pageable);

    @Query(value = """
        SELECT 
            uds.user_id      AS userId,
            u.nickname       AS nickname,
            uds.total_returns AS totalReturns,
            uds.total_asset   AS totalAsset
        FROM user_daily_stats uds
        JOIN users u ON u.user_id = uds.user_id
        WHERE uds.date = :date
    """, nativeQuery = true)
    List<LeaderBoardCandidate> findCandidates(@Param("date") LocalDate date);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    long deleteByDate(LocalDate date);
}
