package SMU.StockMate.domain.userStock.exception;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserStockErrorCode implements BaseErrorCode {

    USER_BALANCE_SHORTAGE(HttpStatus.BAD_REQUEST, "USER_STOCK_001", "사용자의 잔액이 부족합니다."),
    USER_STOCK_STOCK_QUANTITY_EXCEED(HttpStatus.BAD_REQUEST, "USER_STOCK_002", "보유 수량이 부족합니다."),
    USER_STOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER_STOCK_003", "보유 주식을 찾을 수 없습니다."),

    ;



    private final HttpStatus status;
    private final String code;
    private final String message;
}
