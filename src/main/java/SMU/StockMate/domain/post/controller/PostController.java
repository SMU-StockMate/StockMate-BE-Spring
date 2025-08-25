package SMU.StockMate.domain.post.controller;

import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import SMU.StockMate.domain.post.dto.PostListResponse;
import SMU.StockMate.domain.post.dto.PostRequest;
import SMU.StockMate.domain.post.entity.Post;
import SMU.StockMate.domain.post.service.command.PostCommandService;
import SMU.StockMate.domain.post.service.query.PostQueryService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;


    @GetMapping("/{stockCode}")
    @Operation(method = "GET", summary = "게시판 정보 불러오기", description = "해당 주식에 관한 게시글을 불러오는 API 입니다.")
    public CustomResponse<PostListResponse> getPosts(
            @Parameter(description = "주식 종목 코드입니다.")
            @PathVariable String stockCode,

            @Parameter(description = "페이징을 위한 cursor 입니다.")
            @RequestParam(value = "cursor") String cursor
    ) {
        PostListResponse postListResponse = postQueryService.getPosts(cursor, stockCode);

        return CustomResponse.onSuccess(postListResponse);
    }

    @PostMapping("/{stockCode}")
    @Operation(method = "POST", summary = "게시판 글 작성하기", description = "해당 주식에 관한 게시글을 작성하는 API 입니다.")
    public CustomResponse<String> createPost(
            @Parameter(description = "주식 종목 코드입니다.")
            @PathVariable String stockCode,

            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "게시글 request 입니다.")
            @RequestBody PostRequest postRequest
    ) {
        Post post = postCommandService.createPost(stockCode, customUserDetails.getUsername(), postRequest);
        return CustomResponse.onSuccess("\"" + post.getTitle() + "\" 게시글을 생성하였습니다.");
    }

    @PatchMapping("/{postId}")
    @Operation(method = "PATCH", summary = "게시판 글 수정하기", description = "해당 주식에 관한 게시글을 수정하는 API 입니다.")
    public CustomResponse<String> updatePost(
            @Parameter(description = "게시글 Id 입니다.")
            @PathVariable Long postId,

            @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @Parameter(description = "수정할 게시글 request 입니다.")
            @RequestBody PostRequest postRequest
    ) {
        postCommandService.updatePost(postId, customUserDetails.getUsername(), postRequest);
        return CustomResponse.onSuccess("게시글을 업데이트 하였습니다.");
    }

    @DeleteMapping("/{postId}")
    @Operation(method = "DELETE", summary = "게시판 글 삭제하기", description = "해당 주식에 관한 게시글을 삭제하는 API 입니다.")
    public CustomResponse<String> deletePost(
            @Parameter(description = "게시글 Id 입니다.")
            @PathVariable Long postId,

            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postCommandService.deletePost(postId, customUserDetails.getUsername());
        return CustomResponse.onSuccess("게시글을 삭제했습니다.");
    }
}
