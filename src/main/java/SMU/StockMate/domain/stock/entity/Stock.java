package SMU.StockMate.domain.stock.entity;

import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "stock")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code")
    private String stockCode;

    private String standardCode;

    private String koreanName;

    private int basePrice;

//    private boolean isActivate;
}
