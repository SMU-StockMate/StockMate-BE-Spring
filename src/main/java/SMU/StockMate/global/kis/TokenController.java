package SMU.StockMate.global.kis;


import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import SMU.StockMate.global.kis.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kis")
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/token")
    public CustomResponse<TokenResponseDto> getAccessToken() {
        TokenResponseDto token = tokenService.getAccessToken();
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, token);
    }

}
