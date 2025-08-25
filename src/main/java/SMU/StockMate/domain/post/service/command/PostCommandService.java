package SMU.StockMate.domain.post.service.command;

import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import SMU.StockMate.domain.post.dto.PostRequest;
import SMU.StockMate.domain.post.entity.Post;

public interface PostCommandService {

    Post createPost(String stockCode, String email, PostRequest request);

    void updatePost(Long postId, String email, PostRequest request);

    void deletePost(Long postId, String email);
}
