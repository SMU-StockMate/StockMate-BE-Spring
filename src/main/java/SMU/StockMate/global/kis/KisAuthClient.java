package SMU.StockMate.global.kis;
import SMU.StockMate.global.kis.dto.TokenResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KisAuthClient {

    @Value("${kis.api.app-key}")
    private String appKey;

    @Value("${kis.api.app-secret}")
    private String appSecret;

    private final ObjectMapper objectMapper;

    public TokenResponseDto getAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of(
                    "grant_type", "client_credentials",
                    "appkey", appKey,
                    "appsecret", appSecret
            );

            String requestJson = objectMapper.writeValueAsString(body);
            HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

            String url = "https://openapivts.koreainvestment.com:29443/oauth2/tokenP";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            TokenResponseDto token = objectMapper.readValue(response.getBody(), TokenResponseDto.class);
            return token;

        } catch (JsonProcessingException e) {
            log.error("토큰 파싱 실패", e);
            throw new RuntimeException("JSON 파싱 실패");
        } catch (Exception e) {
            log.error("KIS 토큰 발급 실패", e);
            throw new RuntimeException("KIS 요청 실패");
        }
    }
}
