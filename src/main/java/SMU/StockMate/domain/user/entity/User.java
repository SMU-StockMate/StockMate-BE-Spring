package SMU.StockMate.domain.user.entity;

import SMU.StockMate.domain.notification.entity.Notification;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private String email;

    private String password;

    private String nickname;

    private String account; // 계좌 번호

    private Long totalAsset; // 전체 순 자산

    private Long cashBalance; // 예수금

    private Long stockValuation; // 주식 평가 금액

    private Long totalReturns; // 총 수익률

    private String FcmToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}
