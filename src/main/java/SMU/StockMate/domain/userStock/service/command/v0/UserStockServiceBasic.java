package SMU.StockMate.domain.userStock.service.command.v0;

import SMU.StockMate.domain.auth.code.ErrorCode;
import SMU.StockMate.domain.stock.exception.StockErrorCode;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.dto.StockTradingRequest;
import SMU.StockMate.domain.userStock.exception.UserStockErrorCode;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.domain.userStock.repository.UserStockRepository;
import SMU.StockMate.domain.userStock.service.command.UserStockService;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserStockServiceBasic implements UserStockService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;
    private final StockRepository stockRepository;

    @Override
    public void buy(StockTradingRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long totalCost = request.getQuantity() * request.getStockQuote();
        validateUserCashBalance(user, totalCost);

        // 매수하고자 하는 주식을 가지고 있으면 해당 주식을 아니면 새로운 주식 반환
        UserStock userStock = userStockRepository
                .findByUserIdAndStockCode(user.getId(), request.getStockCode())
                .orElseGet(() -> {
                    UserStock newStock = createNewUserStock(user, request);
                    return userStockRepository.save(newStock);
                });

        userStock.addStock(request.getQuantity(), totalCost);
        user.decreaseBalance(totalCost);
    }

    @Override
    public void sell(StockTradingRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long totalCost = request.getQuantity() * request.getStockQuote();


        UserStock userStock = userStockRepository
                .findByUserIdAndStockCode(user.getId(), request.getStockCode())
                .orElseThrow(() -> new CustomException(UserStockErrorCode.USER_STOCK_NOT_FOUND));


        // 보유 수량 보다 매도 수량이 많으면 에러
        validateUserStockQuantity(userStock, request.getQuantity());

        userStock.reduceStock(request.getQuantity(), totalCost);
        user.increaseBalance(totalCost);
    }

    private UserStock createNewUserStock(User user, StockTradingRequest request) {
        Stock stock = stockRepository.findByStockCode(request.getStockCode())
                .orElseThrow(() -> new CustomException(StockErrorCode.STOCK_NOT_FOUND));

        return UserStock.builder()
                .user(user)
                .stock(stock)
                .stockCode(request.getStockCode())
                .quantity(0L)
                .totalAmount(0L)
                .build();
    }

    private void validateUserStockQuantity(UserStock userStock, Long quantity) {
        if (userStock.getQuantity() < quantity) {
            throw new CustomException(UserStockErrorCode.USER_STOCK_STOCK_QUANTITY_EXCEED);
        }
    }

    private void validateUserCashBalance(User user, Long totalCost) {
        if (user.getCashBalance() < totalCost) {
            throw new CustomException(UserStockErrorCode.USER_BALANCE_SHORTAGE);
        }
    }
}
