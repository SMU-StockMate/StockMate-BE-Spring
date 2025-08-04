package SMU.StockMate.domain.stock.query.converter;

import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;
import SMU.StockMate.domain.stock.query.dto.StocksInfoDto;

import java.util.List;

public class StockQueryConverter {
    public static StocksInfoDto toStockInfoDto(Stock stock) {
        return StocksInfoDto.builder()
                .stockCode(stock.getStockCode())
                .koreanName(stock.getKoreanName())
                .basePrice(stock.getBasePrice())
                .build();
    }

    public static StockInfoResponse toStockInfoResponse(List<StocksInfoDto> stocksInfoDtos, String nextCursor) {
        return StockInfoResponse.builder()
                .stockInfos(stocksInfoDtos)
                .nextCursor(nextCursor)
                .build();
    }
}
