package SMU.StockMate.domain.stock.query.service;

import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.query.dto.StockPriceResponseDTO;
import SMU.StockMate.domain.stock.query.repository.StockQueryRepository;
import SMU.StockMate.global.kis.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockPriceServiceImpl implements StockPriceService{
    @Value("${kis.api.app-key}")    private String appKey;
    @Value("${kis.api.app-secret}") private String appSecret;

    private static final String TR_ID = "FHKST01010100";
    private static final String BASE_URL = "https://openapivts.koreainvestment.com:29443";
    private static final String PATH = "/uapi/domestic-stock/v1/quotations/inquire-price";
    private static final DateTimeFormatter KIS_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final StockQueryRepository stockQueryRepository;
    private final TokenService tokenService;

    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public StockPriceResponseDTO getPrice(String stockCode) {

        final Stock stock = stockQueryRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 종목 코드: " + stockCode));

        var token = tokenService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.accessToken());
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", TR_ID);
        headers.set("custtype", "P");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + PATH)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stockCode)
                .build(true)
                .toUri();

        ResponseEntity<Map> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        Map body = Optional.ofNullable(res.getBody())
                .orElseThrow(() -> new IllegalStateException("현재가 응답이 없음"));

        Map<String, Object> output = (Map<String, Object>) body.get("output");
        long current = Long.parseLong(output.get("stck_prpr").toString());
        long change = Long.parseLong(output.get("prdy_vrss").toString());
        double percent = Double.parseDouble(output.get("prdy_ctrt").toString().trim());
        long volume = Long.parseLong(output.get("acml_vol").toString());

        return StockPriceResponseDTO.builder()
                .symbol(stock.getStockCode())
                .name(stock.getKoreanName())
                .price(current)
                .change(change)
                .changePercent(percent)
                .volume(volume)
                .timestamp(OffsetDateTime.now(ZoneId.of("Asia/Seoul"))) // 서울 기준 현재가 조회 시점 표시
                .build();
    }


}
