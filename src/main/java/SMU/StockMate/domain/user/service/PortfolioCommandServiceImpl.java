package SMU.StockMate.domain.user.service;

import SMU.StockMate.domain.user.code.UserErrorCode;
import SMU.StockMate.domain.user.dto.PortfolioResponseDTO;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.command.dto.UserStockResponseDTO;
import SMU.StockMate.domain.userStock.command.repository.UserStockCommandRepository;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioCommandServiceImpl implements PortfolioCommandService {

    private final UserRepository userRepository;
    private final UserStockCommandRepository userStockRepository;

    @Override
    public PortfolioResponseDTO getPortfolio(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        List<UserStockResponseDTO.toPortfolioDTO> userStocks = userStockRepository.findByUserId(user.getId()).stream()
                .map(userStock -> UserStockResponseDTO.toPortfolioDTO.builder()
                        .koreanName(userStock.getStock().getKoreanName())
                        .basePrice(userStock.getStock().getBasePrice())
                        .totalAmount(userStock.getTotalAmount())
                        .avgPrice(userStock.getAvgPrice())
                        .quantity(userStock.getQuantity())
                        .returns(calculateReturns(userStock))
                        .build())
                .toList();

        return PortfolioResponseDTO.builder()
                .nickname(user.getNickname())
                .account(user.getAccount())
                .totalAsset(user.getTotalAsset())
                .cashBalance(user.getCashBalance())
                .stockValuation(user.getStockValuation())
                .totalReturns(user.getTotalReturns())
                .userStockList(userStocks)
                .build();
    }

    private Long calculateReturns(UserStock userStock) {
        int currentPrice = userStock.getStock().getBasePrice();
        Long avgPrice = userStock.getAvgPrice();
        Long quantity = userStock.getQuantity();

        return (currentPrice - avgPrice) * quantity;
    }
}

