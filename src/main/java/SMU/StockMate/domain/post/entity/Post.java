package SMU.StockMate.domain.post.entity;

import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "post")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int likeCount; // 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
