package SMU.StockMate.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDTO(

        @NotBlank(message = "아이디는 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
        String password

) {}

