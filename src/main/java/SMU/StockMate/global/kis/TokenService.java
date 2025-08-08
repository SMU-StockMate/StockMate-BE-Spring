package SMU.StockMate.global.kis;

import SMU.StockMate.global.kis.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final KisAuthClient kisAuthClient;

    public TokenResponseDto getAccessToken() {
        return kisAuthClient.getAccessToken();
    }
}
