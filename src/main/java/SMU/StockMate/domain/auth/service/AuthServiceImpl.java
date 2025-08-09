package SMU.StockMate.domain.auth.service;

import SMU.StockMate.domain.auth.code.ErrorCode;
import SMU.StockMate.domain.auth.dto.SignupRequestDTO;
import SMU.StockMate.domain.auth.dto.TokenResponseDTO;
import SMU.StockMate.domain.auth.entity.RefreshToken;
import SMU.StockMate.domain.auth.jwt.JwtUtil;
import SMU.StockMate.domain.auth.repository.RefreshTokenRepository;
import SMU.StockMate.domain.users.entity.User;
import SMU.StockMate.domain.users.repository.UserRepository;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void signup(SignupRequestDTO request) {
        if (userRepository.existsByEmail(request.username())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER);
        }

        User user = User.builder()
                .email(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);
    }

    @Override
    public TokenResponseDTO reissue(String refreshToken) {
        // 유효성 검증
        if (!jwtUtil.isValid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String username = jwtUtil.getUsername(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_FOUND));

        if (!stored.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 새 AccessToken 발급
        String newAccessToken = jwtUtil.createAccessToken(username);

        return new TokenResponseDTO(newAccessToken, refreshToken);
    }

    @Override
    public void logout(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}
