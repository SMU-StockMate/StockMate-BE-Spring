package SMU.StockMate.domain.userStock.entity;

import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_stock")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockCode;

    private Long quantity; // 보유 수량

    private Long totalAmount; // 총액

    @Column(name = "avg_price")
    private Long avgPrice; // 평균 매수 금액

//    private Long returns; // 수익률

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void addStock(Long quantity, Long totalAmount) {
        this.quantity += quantity;
        this.totalAmount += totalAmount;
        updateAvgPrice();
    }

    public void reduceStock(Long quantity, Long totalAmount) {
        this.quantity -= quantity;
        this.totalAmount -= totalAmount;
        updateAvgPrice();
    }

    private void updateAvgPrice() {
        if (this.quantity > 0) {
            this.avgPrice = this.totalAmount / this.quantity;
        } else {
            this.avgPrice = 0L;
        }
    }
}
