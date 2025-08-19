package SMU.StockMate.domain.userStock.command.dto;

import lombok.Builder;

public class UserStockResponseDTO {

    @Builder
    public record toPortfolioDTO(
            String koreanName,
            int basePrice,
            Long totalAmount, // 총 매수 금액
            Long avgPrice, // 평균 매수 금액
            Long quantity, // 보유 수량
            Long returns // 수익률

    ) {}
}
