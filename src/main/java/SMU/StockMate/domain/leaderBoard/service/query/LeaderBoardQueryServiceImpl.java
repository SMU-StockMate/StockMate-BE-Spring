package SMU.StockMate.domain.leaderBoard.service.query;

import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardDTO;
import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardUserDTO;
import SMU.StockMate.domain.leaderBoard.repository.LeaderBoardRepository;
import SMU.StockMate.domain.user.code.UserErrorCode;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.dto.UserStockResponseDTO;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.domain.userStock.repository.UserStockRepository;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaderBoardQueryServiceImpl implements LeaderBoardQueryService {

    private final LeaderBoardRepository leaderBoardRepository;
    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;

    @Override
    public List<LeaderBoardDTO> getTop100ByDate(LocalDate date) {
        return leaderBoardRepository.findTop100ByDate(date, PageRequest.of(0, 100));
    }

    @Override
    public LeaderBoardUserDTO getUserInLeaderBoard(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // N+1 문제 -> Fetch join 사용해보고 차이점 찾아볼 것
        List<UserStockResponseDTO.toPortfolioDTO> userStocks = userStockRepository.findByUserId(user.getId()).stream()
                .map(userStock -> UserStockResponseDTO.toPortfolioDTO
                        .builder()
                        .koreanName(userStock.getStock().getKoreanName())
                        .basePrice(userStock.getStock().getBasePrice())
                        .totalAmount(userStock.getTotalAmount())
                        .avgPrice(userStock.getAvgPrice())
                        .quantity(userStock.getQuantity())
                        .returns(calculateReturns(userStock))
                        .build())
                .toList();

        return LeaderBoardUserDTO.builder()
                .nickname(user.getNickname())
                .stockValuation(user.getStockValuation())
                .stockTotalBuyAmount(user.getStockTotalBuyAmount())
                .totalReturns(user.getTotalReturns())
                .userStockList(userStocks)
                .build();
    }

    private BigDecimal calculateReturns(UserStock us) {
        // 매입금액 (총 매수 금액)
        BigDecimal buyAmount = BigDecimal.valueOf(us.getTotalAmount() == null ? 0L : us.getTotalAmount());

        if (buyAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO; // 0으로 나눔 방지
        }

        // 평가금액 = 현재가 * 수량
        BigDecimal basePrice = BigDecimal.valueOf(us.getStock().getBasePrice());
        BigDecimal quantity  = BigDecimal.valueOf(us.getQuantity());
        BigDecimal valuation = basePrice.multiply(quantity);

        // 손익 = 평가금액 - 매입금액
        BigDecimal profit = valuation.subtract(buyAmount);

        // 수익률(%) = 손익 / 매입금액 * 100
        return profit
                .multiply(BigDecimal.valueOf(100))
                .divide(buyAmount, 8, RoundingMode.HALF_UP);
    }

}
