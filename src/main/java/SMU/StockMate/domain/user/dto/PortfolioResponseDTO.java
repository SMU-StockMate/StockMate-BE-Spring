package SMU.StockMate.domain.user.dto;

import SMU.StockMate.domain.userStock.command.dto.UserStockResponseDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record PortfolioResponseDTO(
   String nickname,
   String account,
   Long totalAsset,
   Long cashBalance,
   Long stockValuation,
   Long totalReturns,
   List<UserStockResponseDTO.toPortfolioDTO> userStockList
) {}
