package SMU.StockMate.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${Jwt.time.access-expiration}") long accessExpiration,
                   @Value("${Jwt.time.refresh-expiration}") long refreshExpiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = Duration.ofMillis(accessExpiration);
        this.refreshExpiration = Duration.ofMillis(refreshExpiration);
    }

    // ======== 클레임 추출 ========
    // claim은 key, value로 이루어져 있음
    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    // ======== 공통 JWT 파싱 메서드 ========
    // parser -> JWT 해석기, verifyWith -> 이 secretKey 로 해석해라.
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // 두 에러 중 하나라도 터지면 e로 잡아라!!
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }

    public boolean isValid(String token) {
        try {
            if (token == null || token.isBlank()) return false;
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명 위조 여부 검증
                .clockSkewSeconds(60)  // 60초 오차 허용 (서버 간 시간 차이 보완)
                .build()
                .parseSignedClaims(token); // 형식, 서명, 만료, Base64 등 모두 검증
    }

    // ======== 토큰 생성 ========
    public String createAccessToken(String username, Long expiredMs) {
        return creteToken(username, expiredMs);
    }

    public String createRefreshToken(String username, Long expireMs) {
        return creteToken(username, expireMs);
    }

    private String creteToken(String username, Long ms) {
        return Jwts.builder()
                .claim("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ms))
                .signWith(secretKey)
                .compact();
    }
}