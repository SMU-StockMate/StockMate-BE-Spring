package SMU.StockMate.domain.leaderBoard.dto;

import SMU.StockMate.domain.userStock.dto.UserStockResponseDTO;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record LeaderBoardUserDTO(
    String nickname,
    Long stockValuation, // 주식 평가 금액
    Long stockTotalBuyAmount, // 주식 매입 금액
    BigDecimal totalReturns, // 수익률
    List<UserStockResponseDTO.toPortfolioDTO> userStockList
) {}