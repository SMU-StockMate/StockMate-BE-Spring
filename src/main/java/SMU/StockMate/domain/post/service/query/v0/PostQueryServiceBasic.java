package SMU.StockMate.domain.post.service.query.v0;

import SMU.StockMate.domain.post.converter.PostConverter;
import SMU.StockMate.domain.post.dto.PostDto;
import SMU.StockMate.domain.post.dto.PostListResponse;
import SMU.StockMate.domain.post.entity.Post;
import SMU.StockMate.domain.post.repository.PostRepository;
import SMU.StockMate.domain.post.service.query.PostQueryService;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.exception.StockErrorCode;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.user.code.UserErrorCode;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostQueryServiceBasic implements PostQueryService {

    private final StockRepository stockRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Override
    public PostListResponse getPosts(String cursor, String stockCode) {
        // StockCode 로 stockId 을 가져온다
        Long stockId = stockRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new CustomException(StockErrorCode.STOCK_NOT_FOUND)).getId();

        // StockId를 통해 해당 종목의 모든 게시물을 가져옴
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE + 1);
        List<Post> posts;

        if (cursor == null || cursor.isEmpty()){
            posts = postRepository.findAllByStockId(stockId, pageable);
        } else {
            long cursorId = Long.parseLong(cursor);
            posts = postRepository.findAllByStockIdWithCursor(stockId, cursorId, pageable);
        }

        boolean hasMore = posts.size() > DEFAULT_PAGE_SIZE;
        if (hasMore) {
            posts.remove(posts.size() - 1);
        }

        String nextCursor = hasMore ? posts.get(posts.size() - 1).getId().toString() : null;

        List<PostDto> postDtos = posts.stream()
                .map(PostConverter::toPostDto)
                .toList();

        return PostListResponse.builder()
                .postDtos(postDtos)
                .nextCursor(nextCursor)
                .build();
    }
}
