package SMU.StockMate.domain.post.exception;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostErrorCode implements BaseErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404", "존재하지 않는 게시글입니다."),
    POST_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "POST401", "접근할 수 없는 게시글입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
