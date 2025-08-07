package SMU.StockMate.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kis")
@Getter
@Setter
public class KisProperties {
    private String env;          // prod | paper
    private String appkey;
    private String appsecret;
    private Env prod = new Env();
    private Env paper = new Env();
    private Endpoints endpoints = new Endpoints();

    @Getter @Setter public static class Env { private String rest; }
    @Getter @Setter public static class Endpoints {
        private String tokenIssue;
        private String dailyChart;
    }

    public String restBase() {
        return "prod".equalsIgnoreCase(env) ? prod.getRest() : paper.getRest();
    }
}