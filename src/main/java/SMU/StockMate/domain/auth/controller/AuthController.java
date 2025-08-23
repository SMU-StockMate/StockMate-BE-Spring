package SMU.StockMate.domain.auth.controller;

import SMU.StockMate.domain.auth.code.SuccessCode;
import SMU.StockMate.domain.auth.dto.SignupRequestDTO;
import SMU.StockMate.domain.auth.dto.TokenResponseDTO;
import SMU.StockMate.domain.auth.jwt.JwtUtil;
import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import SMU.StockMate.domain.auth.service.AuthService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public CustomResponse<?> signup(@RequestBody @Valid SignupRequestDTO requestDTO) {
        authService.signup(requestDTO);

        return CustomResponse.onSuccess(SuccessCode.SIGNUP_SUCCESS);
    }

    @PostMapping("/reissue")
    public CustomResponse<TokenResponseDTO> reissue(@CookieValue("Refresh-Token") String refreshToken) {
        TokenResponseDTO newToken = authService.reissue(refreshToken);

        return CustomResponse.onSuccess(SuccessCode.TOKEN_REISSUE_SUCCESS, newToken);
    }

    @PostMapping("/logout")
    public CustomResponse<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());

        return CustomResponse.onSuccess(SuccessCode.LOGOUT_SUCCESS);
    }
}
