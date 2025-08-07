package SMU.StockMate.domain.stock.command.exception;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StockLogErrorCode implements BaseErrorCode {

    LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "StockLog_001", "주식 업데이트 로그가 존재하지 않습니다."),
    LOG_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "StockLog_002", "주식 업데이트 중 오류가 발생했습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
