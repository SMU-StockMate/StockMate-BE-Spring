package SMU.StockMate.domain.user.service;

import SMU.StockMate.domain.user.code.UserErrorCode;
import SMU.StockMate.domain.user.dto.PortfolioResponseDTO;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.dto.UserStockResponseDTO;
import SMU.StockMate.domain.userStock.repository.UserStockRepository;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioCommandServiceImpl implements PortfolioCommandService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;

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

    private BigDecimal calculateReturns(UserStock us) {
        long qty  = us.getQuantity() == null ? 0L : us.getQuantity();
        Integer base = (us.getStock() == null) ? null : us.getStock().getBasePrice();
        long buy = us.getTotalAmount() == null ? 0L : us.getTotalAmount();

        // 오류/비정상 데이터 방어: 매입금액, 수량, 현재가가 유효하지 않으면 0%
        if (buy <= 0L || qty <= 0L || base == null || base <= 0) {
            return BigDecimal.ZERO.setScale(8);
        }

        BigDecimal buyAmount = BigDecimal.valueOf(buy);
        BigDecimal valuation = BigDecimal.valueOf(base).multiply(BigDecimal.valueOf(qty));
        BigDecimal profit = valuation.subtract(buyAmount);

        // 수익률(%) = 손익 / 매입 * 100
        return profit.multiply(BigDecimal.valueOf(100))
                .divide(buyAmount, 8, RoundingMode.HALF_UP);
    }
}

