package SMU.StockMate.domain.post.service.query;

import SMU.StockMate.domain.post.dto.PostListResponse;

public interface PostQueryService {

    PostListResponse getPosts(String cursor, String stockCode);
}
