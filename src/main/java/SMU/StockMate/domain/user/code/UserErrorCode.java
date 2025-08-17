package SMU.StockMate.domain.user.code;

import SMU.StockMate.global.apiPayload.code.error.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements BaseErrorCode {

    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER40901", "이미 존재하는 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER40902", "이미 존재하는 닉네임입니다."),
    USER_ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER40903", "이미 존재하는 계좌번호입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
