package SMU.StockMate.domain.stock.entity;

import SMU.StockMate.domain.stock.entity.enums.StockUpdateStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "stock_update_log")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StockUpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate updateDate;

    private int totalCount;

    @Enumerated(EnumType.STRING)
    private StockUpdateStatus status;

    public void updateStockLog(int totalCount, StockUpdateStatus status) {
        this.totalCount = totalCount;
        this.status = status;
    }
}
