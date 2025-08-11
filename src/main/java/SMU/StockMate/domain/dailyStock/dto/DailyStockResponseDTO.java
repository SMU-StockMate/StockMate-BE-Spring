package SMU.StockMate.domain.dailyStock.dto;

import SMU.StockMate.domain.dailyStock.entity.DailyStock;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStockResponseDTO {

    private String stockCode; // 종목 코드
    private LocalDate tradeDate; // 거래일 (일봉 데이터 요청을 위해 추가)
    private Long openPrice; // 시가
    private Long highPrice; // 고가
    private Long lowPrice; // 저가
    private Long closePrice; // 종가
    private Long volume; // 거래량;

    public static DailyStockResponseDTO from(DailyStock dailyStock){
        return DailyStockResponseDTO.builder()
                .stockCode(dailyStock.getStock().getStockCode())
                .tradeDate(dailyStock.getCreatedAt().toLocalDate())
                .openPrice(dailyStock.getOpenPrice())
                .highPrice(dailyStock.getHighPrice())
                .lowPrice(dailyStock.getLowPrice())
                .closePrice(dailyStock.getClosePrice())
                .volume(dailyStock.getVolume())
                .build();
    }


}
