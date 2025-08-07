package SMU.StockMate.domain.stock.command.exception;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ParsingErrorCode implements BaseErrorCode{

    HTTP_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "PARSING_001", "주식 마스터 파일 다운로드 중 클라이언트 에러가 발생했습니다."),
    HTTP_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PARSING_002", "주식 마스터 파일 서버에서 응답 에러가 발생했습니다.")

    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}
