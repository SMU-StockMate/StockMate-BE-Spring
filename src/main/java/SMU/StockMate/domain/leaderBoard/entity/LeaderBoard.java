package SMU.StockMate.domain.leaderBoard.entity;

import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "leader_board")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LeaderBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // 순위 날짜
    private Integer ranking;

    private Long userId;
    private String username;
    private Long totalAsset;
    private BigDecimal totalReturns;

}
