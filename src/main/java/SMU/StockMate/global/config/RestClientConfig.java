package SMU.StockMate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
//                .requestFactory(/* 필요시 커넥션 풀 설정 */)
                .build();
    }
}
