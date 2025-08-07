package SMU.StockMate.domain.dailyStock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DailyStockService {
    private final WebClient kisWebClient;
    private final KisTokenService tokenService;
    private final KisProperties props;

    private static final String TR_ID = "FHKST03010100"; // 기간별 시세 조회용
    private static final String MARKET_J = "J";          // 주식/ETF/ETN

    public Mono<DailyPriceResponse> inquireDaily(DailyPriceRequest req) {
        return tokenService.getAccessToken().flatMap(token ->
                kisWebClient.get()
                        .uri(uriBuilder -> {
                            var u = uriBuilder.path(props.getEndpoints().getDailyChart())
                                    .queryParam("FID_COND_MRKT_DIV_CODE", MARKET_J)
                                    .queryParam("FID_INPUT_ISCD", req.symbol())
                                    .queryParam("FID_PERIOD_DIV_CODE", req.period())
                                    .queryParam("FID_ORG_ADJ_PRC", req.adjusted() ? "1" : "0");
                            if (req.from() != null) u.queryParam("FID_INPUT_DATE_1", req.from());
                            if (req.to()   != null) u.queryParam("FID_INPUT_DATE_2", req.to());
                            return u.build();
                        })
                        .headers(h -> {
                            h.setBearerAuth(token);
                            h.add("appkey", props.getAppkey());
                            h.add("appsecret", props.getAppsecret());
                            h.add("tr_id", TR_ID);
                            h.add("custtype", "P"); // 개인
                        })
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, r ->
                                r.bodyToMono(String.class).flatMap(b -> Mono.error(new IllegalStateException("KIS error: " + b))))
                        .bodyToMono(DailyPriceResponse.class)
        );
    }
}

