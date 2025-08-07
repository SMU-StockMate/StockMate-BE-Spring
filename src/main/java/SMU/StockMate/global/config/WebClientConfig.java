package SMU.StockMate.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final KisProperties props;

    @Bean
    public WebClient kisWebClient() {
        // 큰 응답 대비 버퍼 상향(필요 시)
        var strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(props.restBase())
                .exchangeStrategies(strategies)
                .defaultHeaders(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
}