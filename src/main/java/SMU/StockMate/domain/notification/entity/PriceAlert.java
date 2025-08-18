package SMU.StockMate.domain.notification.entity;

import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PriceAlert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String stockCode;
    private Long targetPrice; //목표 각격
    private boolean triggered = false; // 달성 여부
}