package SMU.StockMate.domain.stock.converter;

import SMU.StockMate.domain.stock.dto.StockCodeDto;
import SMU.StockMate.domain.stock.dto.StockSearchListResponse;
import SMU.StockMate.domain.stock.dto.StocksInfoDto;
import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.stereotype.Component;

import java.util.List;

public class StockConverter {
    public static Stock of(StockCodeDto stockCodeDto) {
        return Stock.builder()
                .stockCode(stockCodeDto.getShortCode())
                .standardCode(stockCodeDto.getStandardCode())
                .koreanName(stockCodeDto.getKoreanName())
                .basePrice(stockCodeDto.getBasePrice())
                .build();
    }

    public static StocksInfoDto toStockInfoDto(Stock stock) {
        return StocksInfoDto.builder()
                .stockCode(stock.getStockCode())
                .koreanName(stock.getKoreanName())
                .basePrice(stock.getBasePrice())
                .build();
    }

    public static StockSearchListResponse toStockInfoResponse(List<StocksInfoDto> stocksInfoDtos, String nextCursor) {
        return StockSearchListResponse.builder()
                .stockInfos(stocksInfoDtos)
                .nextCursor(nextCursor)
                .build();
    }
}
