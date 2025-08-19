package SMU.StockMate.domain.stock.code;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StockErrorCode implements BaseErrorCode {

    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK404", "존재하지 않는 주식코드입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
