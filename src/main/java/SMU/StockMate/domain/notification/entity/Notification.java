package SMU.StockMate.domain.notification.entity;

import SMU.StockMate.domain.user.entity.User;
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

//    private String message; 대신
    private String title;
    private String body;

    @Column(name = "is_read")
    private boolean isRead = false;

    public void setIsRead(boolean isRead){
        this.isRead=isRead;
    }

//    @Enumerated(EnumType.STRING)
//    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
