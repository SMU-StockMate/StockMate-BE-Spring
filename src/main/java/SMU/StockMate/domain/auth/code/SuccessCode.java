package SMU.StockMate.domain.auth.code;

import SMU.StockMate.global.apiPayload.code.success.BaseSuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements BaseSuccessCode {
    // 인증 관련
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH200", "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH201", "로그아웃에 성공했습니다."),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "AUTH202", "Access Token이 재발급되었습니다."),

    // 회원 관련
    SIGNUP_SUCCESS(HttpStatus.CREATED, "USER201", "회원가입에 성공했습니다."),
    USER_INFO_RETRIEVED(HttpStatus.OK, "USER200", "회원 정보를 성공적으로 조회했습니다."),
    USER_UPDATED(HttpStatus.OK, "USER204", "회원 정보가 성공적으로 수정되었습니다."),
    USER_DELETED(HttpStatus.OK, "USER205", "회원 탈퇴가 성공적으로 처리되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
