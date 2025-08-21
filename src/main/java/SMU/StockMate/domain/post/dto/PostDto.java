package SMU.StockMate.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDto {
    private String nickname; // 작성자 이름
    private String createDateBefore; // 몇 시간 전에 생성된는지
    private String title;
    private String content;
    private int likeCount;
}
