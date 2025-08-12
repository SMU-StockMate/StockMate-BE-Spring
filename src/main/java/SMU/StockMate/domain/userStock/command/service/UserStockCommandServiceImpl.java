package SMU.StockMate.domain.userStock.command.service;

import SMU.StockMate.domain.auth.code.ErrorCode;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.command.dto.StockTradingRequest;
import SMU.StockMate.domain.userStock.command.exception.UserStockErrorCode;
import SMU.StockMate.domain.userStock.command.repository.UserStockCommandRepository;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserStockCommandServiceImpl implements UserStockCommandService {

    private final UserRepository userRepository;
    private final UserStockCommandRepository userStockCommandRepository;

    @Override
    public void buy(StockTradingRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long totalCost = request.getQuantity() * request.getStockQuote();
        validateUserCashBalance(user, totalCost);

        // 매수하고자 하는 주식을 가지고 있으면 해당 주식을 아니면 새로운 주식 반환
        UserStock userStock = userStockCommandRepository
                .findByUserIdAndStockCode(user.getId(), request.getStockCode())
                .orElseGet(() -> {
                    UserStock newStock = createNewUserStock(user, request);
                    return userStockCommandRepository.save(newStock);
                });

        userStock.addStock(request.getQuantity(), totalCost);
        user.decreaseBalance(totalCost);
    }

    @Override
    public void sell(StockTradingRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long totalCost = request.getQuantity() * request.getStockQuote();


        UserStock userStock = userStockCommandRepository
                .findByUserIdAndStockCode(user.getId(), request.getStockCode())
                .orElseThrow(() -> new CustomException(UserStockErrorCode.USER_STOCK_NOT_FOUND));


        // 보유 수량 보다 매도 수량이 많으면 에러
        validateUserStockQuantity(userStock, request.getQuantity());

        userStock.reduceStock(request.getQuantity(), totalCost);
        user.increaseBalance(totalCost);
    }

    private UserStock createNewUserStock(User user, StockTradingRequest request) {
        return UserStock.builder()
                .user(user)
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
