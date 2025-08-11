package SMU.StockMate.domain.dailyStock.service;

import SMU.StockMate.domain.dailyStock.dto.DailyStockResponseDTO;
import SMU.StockMate.domain.dailyStock.entity.DailyStock;
import SMU.StockMate.domain.dailyStock.repository.DailyStockRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.query.repository.StockQueryRepository;
import SMU.StockMate.global.kis.TokenService;
import SMU.StockMate.global.kis.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyStockServiceImpl implements DailyStockService {

    @Value("${kis.api.app-key}")    private String appKey;
    @Value("${kis.api.app-secret}") private String appSecret;

    private static final String TR_ID = "FHKST03010100";
    private static final String BASE_URL = "https://openapivts.koreainvestment.com:29443";
    private static final String PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
    private static final DateTimeFormatter KIS_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final StockQueryRepository stockQueryRepository;
    private final TokenService tokenService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final DailyStockRepository dailyStockRepository;

    @Transactional
    @Override
    public List<DailyStockResponseDTO> getDailyStock(String stockCode, LocalDate from, LocalDate to) {
        // 종목 코드 확인
        final Stock stock = stockQueryRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 종목 코드: " + stockCode));

        // DB 조회 후 이미 있는 데이터인지 확인
        List<DailyStock> cached = dailyStockRepository
                .findByStockAndTradeDateBetweenOrderByTradeDateAsc(stock, from, to);
        if (!cached.isEmpty()) {
            return toDtoList(cached);
        }

        // 없으면 저장
        return dailyStore(stock, from, to);
    }

    @Transactional
    protected List<DailyStockResponseDTO> dailyStore(Stock stock, LocalDate from, LocalDate to) {
        // 한투 토큰 가져오기
        var token = tokenService.getAccessToken();

        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.accessToken());
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", TR_ID);
        headers.set("custtype", "P");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // URI
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + PATH)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stock.getStockCode())
                .queryParam("FID_INPUT_DATE_1", from.format(KIS_FMT))
                .queryParam("FID_INPUT_DATE_2", to.format(KIS_FMT))
                .queryParam("FID_PERIOD_DIV_CODE", "D")
                .queryParam("FID_ORG_ADJ_PRC", "1")
                .build(true).toUri();

        ResponseEntity<Map> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        Map body = Optional.ofNullable(res.getBody())
                .orElseThrow(() -> new IllegalStateException("응답이 비었습니다."));

        @SuppressWarnings("unchecked") //강제 캐스팅으로 인한 경고를 막아주는 용도
        List<Map<String, Object>> rows = (List<Map<String, Object>>) body.get("output2");
        if (rows == null || rows.isEmpty()) return Collections.emptyList();


        List<DailyStock> entities = new ArrayList<>(rows.size());

        for (Map<String, Object> item : rows) {
            LocalDate tradeDate = LocalDate.parse(String.valueOf(item.get("stck_bsop_date")), KIS_FMT);

            Long open  = Long.valueOf(item.get("stck_oprc").toString().trim());
            Long high = Long.valueOf(item.get("stck_hgpr").toString().trim());
            Long low   = Long.valueOf(item.get("stck_lwpr").toString().trim());
            Long close = Long.valueOf(item.get("stck_clpr").toString().trim());
            Long vol   = Long.valueOf(item.get("acml_vol").toString().trim());

            DailyStock e = DailyStock.builder()
                    .stock(stock)
                    .tradeDate(tradeDate)
                    .openPrice(open)
                    .highPrice(high)
                    .lowPrice(low)
                    .closePrice(close)
                    .volume(vol)
                    .build();
            entities.add(e);
        }

        // 일봉 데이터 저장
        try {
            dailyStockRepository.saveAll(entities);
        } catch (org.springframework.dao.DataIntegrityViolationException dup) {
            for (DailyStock e : entities) {
                try {
                    dailyStockRepository.save(e);
                } catch (org.springframework.dao.DataIntegrityViolationException ignore) {
                    // 이미 있음 → 무시
                }
            }
        }

        // DTO 반환
        List<DailyStock> saved = dailyStockRepository
                .findByStockAndTradeDateBetweenOrderByTradeDateAsc(stock, from, to);
        return toDtoList(saved);
    }

    // DTO 변환
    private List<DailyStockResponseDTO> toDtoList(List<DailyStock> list) {
        List<DailyStockResponseDTO> result = new ArrayList<>(list.size());
        for (DailyStock d : list) {
            result.add(DailyStockResponseDTO.builder()
                    .stockCode(d.getStock().getStockCode())
                    .tradeDate(d.getTradeDate())
                    .openPrice(d.getOpenPrice())
                    .highPrice(d.getHighPrice())
                    .lowPrice(d.getLowPrice())
                    .closePrice(d.getClosePrice())
                    .volume(d.getVolume())
                    .build());
        }
        return result;
    }

    @Override
    public List<DailyStockResponseDTO> getLast30Days(String stockCode) {
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(30);
        return getDailyStock(stockCode, from, to);
    }
}
