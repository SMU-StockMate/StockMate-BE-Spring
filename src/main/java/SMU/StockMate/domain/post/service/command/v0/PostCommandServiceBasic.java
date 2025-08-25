package SMU.StockMate.domain.post.service.command.v0;

import SMU.StockMate.domain.post.converter.PostConverter;
import SMU.StockMate.domain.post.dto.PostRequest;
import SMU.StockMate.domain.post.entity.Post;
import SMU.StockMate.domain.post.exception.PostErrorCode;
import SMU.StockMate.domain.post.repository.PostRepository;
import SMU.StockMate.domain.post.service.command.PostCommandService;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.exception.StockErrorCode;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.user.code.UserErrorCode;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostCommandServiceBasic implements PostCommandService {

    private final PostRepository postRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(String stockCode, String email, PostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        Stock stock = stockRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new CustomException(StockErrorCode.STOCK_NOT_FOUND));

        Post post = PostConverter.of(request, user, stock);

        return postRepository.save(post);
    }

    @Override
    public void updatePost(Long postId, String email, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        // 글을 작성한 본인인지 검증
        post.validateAuthor(email);

        post.updatePost(request);
    }

    @Override
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.POST_NOT_FOUND));

        // 글을 작성한 본인인지 검증
        post.validateAuthor(email);

        postRepository.delete(post);
    }
}
