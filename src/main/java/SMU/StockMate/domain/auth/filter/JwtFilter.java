package SMU.StockMate.domain.auth.filter;

import SMU.StockMate.domain.auth.dto.UserDetailsDTO;
import SMU.StockMate.domain.auth.jwt.JwtUtil;
import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // 1. Authorization 헤더 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. JWT 추출
        String token = authorizationHeader.split(" ")[1];

        // 3. JWT 유효성 검사
        if (!jwtUtil.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 유저 정보 추출
        String username = jwtUtil.getUsername(token);

        // 5. UserDetails 객체 생성
        UserDetailsDTO user = UserDetailsDTO.builder()
                .email(username)
                .password("N/A")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 6. 인증 객체 생성 및 SecurityContext에 등록
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 7. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
