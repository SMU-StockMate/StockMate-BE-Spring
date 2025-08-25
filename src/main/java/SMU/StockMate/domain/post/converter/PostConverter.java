package SMU.StockMate.domain.post.converter;

import SMU.StockMate.domain.post.dto.PostDto;
import SMU.StockMate.domain.post.dto.PostRequest;
import SMU.StockMate.domain.post.entity.Post;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.user.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class PostConverter {

    public static Post of(PostRequest request, User user, Stock stock) {
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .stock(stock)
                .user(user)
                .build();
    }

    public static PostDto toPostDto(Post post) {
        String createDateBefore = getCreateDateBefore(post);

        return PostDto.builder()
                .nickname(post.getUser().getNickname())
                .createDateBefore(createDateBefore)
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .build();
    }

    // 현재 날짜와 게시물 업데이트 일의 차이 구하기
    private static String getCreateDateBefore(Post post) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updatedAt = post.getUpdatedAt();

        Duration duration = Duration.between(updatedAt, now);

        String createDateBefore;

        long diffInSeconds = duration.getSeconds();

        if (diffInSeconds < 60) {
            // 1분 미만: "방금 전" 또는 "n초 전"
            createDateBefore = "방금 전";
        } else if (diffInSeconds < 3600) {
            // 1시간 미만: "n분 전"
            long minutes = duration.toMinutes();
            createDateBefore = minutes + "분 전";
        } else if (diffInSeconds < 86400) {
            // 1일 미만: "n시간 전"
            long hours = duration.toHours();
            createDateBefore = hours + "시간 전";
        } else if (diffInSeconds < 31536000L) { // 1년 미만: "n일 전"
            // 365일을 1년으로 가정
            long days = duration.toDays();
            createDateBefore = days + "일 전";
        } else {
            // 1년 이상: "n년 전"
            long years = duration.toDays() / 365; // 대략적인 계산
            createDateBefore = years + "년 전";
        }
        return createDateBefore;
    }
}
