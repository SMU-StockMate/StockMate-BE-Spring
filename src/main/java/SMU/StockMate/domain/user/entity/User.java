package SMU.StockMate.domain.user.entity;

import SMU.StockMate.domain.notification.entity.Notification;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String account; // 계좌 번호

    private Long totalAsset; // 전체 순 자산

    private Long cashBalance; // 예수금

    @Builder.Default
    private Long stockValuation = 0L; // 주식 평가 금액

    @Builder.Default
    private Long stockTotalBuyAmount = 0L; // 주식 매입 금액

    @Builder.Default
    private BigDecimal totalReturns = BigDecimal.ZERO; // 총 수익률

    private String FcmToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // 주식을 매수 한 경우
    public void decreaseBalance(Long cost) {
        this.stockValuation += cost;
        this.cashBalance -= cost;
        this.stockTotalBuyAmount += cost;
    }

    // 주식을 매도한 경우(cost -> 주식 평가 금액, buyCost -> 주식 매입 금액)
    public void increaseBalance(Long cost, Long buyCost) {
        this.stockValuation -= cost;
        this.cashBalance += cost;
    }
}
