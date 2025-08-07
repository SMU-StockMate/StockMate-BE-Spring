package SMU.StockMate.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KisTokenService {
    private final WebClient kisWebClient;
    private final KisProperties props;

    private volatile String cachedToken;
    private volatile Instant expiresAt;

    public Mono<String> getAccessToken() {

        if (cachedToken != null && expiresAt != null &&
                Instant.now().isBefore(expiresAt.minusSeconds(60))) {
            return Mono.just(cachedToken);
        }

        return kisWebClient.post()
                .uri(props.getEndpoints().getTokenIssue())
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "appkey", props.getAppkey(),
                        "appsecret", props.getAppsecret()
                ))
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).flatMap(b -> Mono.error(new IllegalStateException("Token error: " + b))))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(resp -> {
                    String token = (String) resp.get("access_token");
                    int ttl = ((Number) resp.getOrDefault("expires_in", 86400)).intValue();
                    this.cachedToken = token;
                    this.expiresAt = Instant.now().plusSeconds(ttl);
                    return token;
                });
    }
}