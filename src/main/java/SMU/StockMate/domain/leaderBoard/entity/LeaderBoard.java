package SMU.StockMate.domain.leaderBoard.entity;

import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(
        name = "leader_board",
        uniqueConstraints = {
                // 같은 날짜에 동일한 순위, 동일한 유저가 없도록 제약 조건
                @UniqueConstraint(name = "unique_leaderboard_date_rank", columnNames = {"date","rank_no"}),
                @UniqueConstraint(name = "uq_leaderboard_date_user", columnNames = {"date", "user_id"})
        },
        indexes = {
                @Index(name = "idx_leaderboard_date", columnList = "date"),
        }

)
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaderBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // 순위 날짜

    @Column(name = "rank_no",nullable = false)
    private Integer rank; // 1~100등 까지

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(name = "total_asset", nullable = false)
    private Long totalAsset;

    @Column(name = "total_returns", nullable = false, precision = 18, scale = 8) // 전체 자리 = 18, 소수점 자리 = 8
    private BigDecimal totalReturns;

}
