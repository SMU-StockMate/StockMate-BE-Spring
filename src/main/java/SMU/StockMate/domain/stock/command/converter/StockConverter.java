package SMU.StockMate.domain.stock.command.converter;

import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockConverter {
    public Stock toStock(StockCodeDto stockCodeDto) {
        return Stock.builder()
                .stockCode(stockCodeDto.getShortCode())
                .standardCode(stockCodeDto.getStandardCode())
                .koreanName(stockCodeDto.getKoreanName())
                .basePrice(stockCodeDto.getBasePrice())
                .build();
    }
}
