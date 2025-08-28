package SMU.StockMate.domain.userStock.dto;

import lombok.Builder;

import java.math.BigDecimal;

public class UserStockResponseDTO {

    @Builder
    public record toPortfolioDTO(
            String koreanName,
            int basePrice,
            Long totalAmount, // 총 매수 금액
            Long avgPrice, // 평균 매수 금액
            Long quantity, // 보유 수량
            BigDecimal returns // 수익률
    ) {}
}
