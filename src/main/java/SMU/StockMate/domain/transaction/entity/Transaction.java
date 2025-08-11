package SMU.StockMate.domain.transaction.entity;

import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.transaction.enums.Type;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "transaction")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity; // 매수량

    private Long price; // 매매가

    @Column(name = "total_amount")
    private Long totalAmount; // 총액

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
