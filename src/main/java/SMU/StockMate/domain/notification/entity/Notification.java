package SMU.StockMate.domain.notification.entity;

import SMU.StockMate.domain.notification.enums.Type;
import SMU.StockMate.domain.users.entity.Users;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "notification")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(name = "is_read")
    private boolean isRead = false;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;
}
