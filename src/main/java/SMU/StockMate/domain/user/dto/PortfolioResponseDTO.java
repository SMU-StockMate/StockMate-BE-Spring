package SMU.StockMate.domain.user.dto;

import SMU.StockMate.domain.userStock.dto.UserStockResponseDTO;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PortfolioResponseDTO(
   String nickname,
   String account,
   Long totalAsset,
   Long cashBalance,
   Long stockValuation,
   BigDecimal totalReturns,
   List<UserStockResponseDTO.toPortfolioDTO> userStockList
) {}
