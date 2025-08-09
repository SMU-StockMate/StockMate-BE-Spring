package SMU.StockMate.domain.auth.service;

import SMU.StockMate.domain.auth.dto.SignupRequestDTO;
import SMU.StockMate.domain.auth.dto.TokenResponseDTO;

public interface AuthService {
    void signup(SignupRequestDTO request);
    TokenResponseDTO reissue(String refreshToken);
    void logout(String username);
}
