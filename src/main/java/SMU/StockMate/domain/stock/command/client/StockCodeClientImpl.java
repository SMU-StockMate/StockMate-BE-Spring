package SMU.StockMate.domain.stock.command.client;

import SMU.StockMate.domain.stock.command.dto.MarketParsingRule;
import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.command.exception.ParsingErrorCode;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@RequiredArgsConstructor
public class StockCodeClientImpl implements StockCodeClient {

    private final WebClient webClient;

    /**
     * 코스피 주식 정보를 가져옴
     * @return
     */
    @Override
    public List<StockCodeDto> getKospiCode() {
        return getStockMst(
                "https://new.real.download.dws.co.kr/common/master/kospi_code.mst.zip",
                MarketParsingRule.KOSPI
        );
    }

    /**
     * 코스닥 주식 정보를 가져옴
     * @return
     */
    @Override
    public List<StockCodeDto> getKosdacCode() {
        return getStockMst(
                "https://new.real.download.dws.co.kr/common/master/kosdaq_code.mst.zip",
                MarketParsingRule.KOSDAC
        );
    }

    /**
     * uri를 찾아가 zip 파일을 가져옴
     * @param uri
     * @param rule
     * @return
     */
    private List<StockCodeDto> getStockMst(String uri, MarketParsingRule rule) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException(ParsingErrorCode.HTTP_CLIENT_ERROR)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException(ParsingErrorCode.HTTP_SERVER_ERROR)))
                .bodyToMono(byte[].class)
                .map(zipData -> parseZipFile(zipData, rule))
                .block();
    }

    //TODO: 두개 비동기 적으로 합치기

    /**
     * zip 파일을 읽어 .mst로 끝나는 파일을 parseMstFile 넘겨서 정보 받아오기
     *
     * @param zipData
     * @return StockCodeDto
     */
    private List<StockCodeDto> parseZipFile(byte[] zipData, MarketParsingRule rule) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))) {
            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".mst")) {
                    return parseMstFile(zis, rule);
                }
            }
        } catch (IOException e) {
            // 에러 던지기
        }

        return List.of();
    }

    /**
     * mst 파일을 line 별로 읽어서 parseLineToStockCodeDto을 통해 정보 추출하여 list에 저장
     *
     * @param inputStream
     * @return
     */
    private List<StockCodeDto> parseMstFile(InputStream inputStream, MarketParsingRule rule) {
        List<StockCodeDto> codes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "CP949"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                StockCodeDto stockCode = parseLineToStockCodeDto(line, rule);

                if (stockCode != null) {
                    codes.add(stockCode);
                }
            }
        } catch (IOException e) {
            // 에러 던지기
        }

        return codes;
    }

    /**
     * line 예시
     * frontPart
     * - 035720   KR7035720002카카오
     * backPart: MarketingRule.backPartLength
     * - ST1002900000000 NNBYYY YYNNNNNNN0NNNNNNNN0000580000000100001NNN00NNN000000020Y0900000020623820000000001002017071000000000044201300000000004425137220012       0 NYY00001863700000105400000192902003000006.7020250331000256367KAONNY
     */
    private StockCodeDto parseLineToStockCodeDto(String line, MarketParsingRule rule) {
        // 앞부분 추출
        String frontPart = line.substring(0, line.length() - rule.getBackPartLength());

        // 단축 코드
        String shortCode = frontPart.substring(rule.getShortCodeStart(), rule.getShortCodeEnd()).trim();
        //표준 코드
        String standardCode = frontPart.substring(rule.getShortCodeEnd(), rule.getStandardCodeEnd()).trim();
        //한글명
        String koreanName = frontPart.substring(rule.getStandardCodeEnd()).trim();


        // 뒷부분 228바이트
        String backPart = line.substring(line.length() - rule.getBackPartLength());
        // 기준가(전날 종가)
        int basePriceStr = Integer.parseInt(backPart.substring(rule.getBasePriceStart(), rule.getBasePriceEnd()).trim());

        return StockCodeDto.builder()
                .shortCode(shortCode)
                .standardCode(standardCode)
                .koreanName(koreanName)
                .basePrice(basePriceStr)
                .build();

    }

}
