package SMU.StockMate.domain.auth.dto;

import jakarta.validation.constraints.*;

public record SignupRequestDTO(

        @NotBlank(message = "아이디는 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(max = 15, message = "비밀번호는 최대 15자 이하이어야 합니다.")
        String nickname,

        @NotBlank(message = "계좌번호는 필수입니다.")
        String account,

        @NotNull(message = "예수금 설정은 필수입니다.")
        @Min(value = 1_000_000, message = "예수금은 최소 100만원 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "예수금은 최대 1억원 이하여야 합니다.")
        Long cashBalance
) {}

