package SMU.StockMate.domain.post.entity;

import SMU.StockMate.domain.post.dto.PostRequest;
import SMU.StockMate.domain.post.exception.PostErrorCode;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import SMU.StockMate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private int likeCount = 0; // 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void validateAuthor(String email) {
        if (!this.user.getEmail().equals(email)) {
            throw new CustomException(PostErrorCode.POST_ACCESS_DENIED);
        }
    }

    public void updatePost(PostRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }
}
