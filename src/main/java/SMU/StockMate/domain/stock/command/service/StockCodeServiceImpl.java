package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@RequiredArgsConstructor
public class StockCodeServiceImpl implements StockCodeService {

    private final WebClient webClient;

    // 코스피 코드의 정보가 담겨있는 zip 파일을 가져옴
    @Override
    public List<StockCodeDto> retrieveKospiMst() {
        return webClient.get()
                .uri("https://new.real.download.dws.co.kr/common/master/kospi_code.mst.zip")
                .retrieve()
                .bodyToMono(byte[].class)
                .map(this::parseZipFile)
                .block();
    }

//    @Override
//    public List<StockCodeDto> retrieveKosdacMst() {
//
//    }

    /**
     * zip 파일을 읽어 .mst로 끝나는 파일을 parseMstFile 넘겨서 정보 받아오기
     * @param zipData
     * @return StockCodeDto
     */
    private List<StockCodeDto> parseZipFile(byte[] zipData) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipData))){
            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".mst")) {
                    return parseMstFile(zis);
                }
            }
        } catch (IOException e) {
            // 에러 던지기
        }

        return List.of();
    }

    /**
     * mst 파일을 line 별로 읽어서 parseLineToStockCodeDto을 통해 정보 추출하여 list에 저장
     * @param inputStream
     * @return
     */
    private List<StockCodeDto> parseMstFile(InputStream inputStream) {
        List<StockCodeDto> codes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "CP949"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                StockCodeDto stockCode = parseLineToStockCodeDto(line);

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
     * backPart: 228자
     * - ST1002900000000 NNBYYY YYNNNNNNN0NNNNNNNN0000580000000100001NNN00NNN000000020Y0900000020623820000000001002017071000000000044201300000000004425137220012       0 NYY00001863700000105400000192902003000006.7020250331000256367KAONNY
     */
    private StockCodeDto parseLineToStockCodeDto(String line) {
        // 앞부분 추출
        String frontPart = line.substring(0, line.length() - 228);

        // 단축 코드
        String shortCode = frontPart.substring(0, 9).trim();
        //표준 코드
        String standardCode = frontPart.substring(9, 21).trim();
        //한글명
        String koreanName = frontPart.substring(21).trim();


        // 뒷부분 228바이트
        String backPart = line.substring(line.length() - 228);
        // 기준가(전날 종가)
        int basePriceStr = Integer.parseInt(backPart.substring(42, 51).trim());

        return StockCodeDto.builder()
                .shortCode(shortCode)
                .standardCode(standardCode)
                .koreanName(koreanName)
                .basePrice(basePriceStr)
                .build();

    }

}
