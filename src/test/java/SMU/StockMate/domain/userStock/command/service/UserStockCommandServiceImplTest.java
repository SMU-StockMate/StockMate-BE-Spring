package SMU.StockMate.domain.userStock.command.service;

import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import SMU.StockMate.domain.userStock.dto.StockTradingRequest;
import SMU.StockMate.domain.userStock.exception.UserStockErrorCode;
import SMU.StockMate.domain.userStock.repository.UserStockRepository;
import SMU.StockMate.domain.userStock.entity.UserStock;
import SMU.StockMate.domain.userStock.service.UserStockServiceImpl;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStockCommandServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserStockRepository userStockCommandRepository;

    @InjectMocks
    UserStockServiceImpl userStockCommandService;

    private User user;
    private StockTradingRequest tradingRequest;
    private UserStock existingUserStock;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test")
                .cashBalance(100000L)
                .stockValuation(100000L)
                .build();

        tradingRequest = createStockTradingRequest("카카오", 1000L, 10L);

        existingUserStock = UserStock.builder()
                .id(1L)
                .user(user)
                .stockCode("카카오")
                .quantity(20L)
                .totalAmount(20000L)
                .avgPrice(1000L)
                .build();
    }

    @Test
    @DisplayName("매수 실패 - 잔액 부족")
    void buy_stock_not_enough_balance() throws Exception{
        // given
        User notEnoughBalanceUser = User.builder()
                .id(2L)
                .email("poor")
                .cashBalance(5000L)
                .totalAsset(5000L)
                .build();
        when(userRepository.findByEmail("poor")).thenReturn(Optional.ofNullable(notEnoughBalanceUser));

        // when, then
        assertThatThrownBy(() -> userStockCommandService.buy(tradingRequest, "poor"))
                .isInstanceOf(CustomException.class)
                .hasMessage(UserStockErrorCode.USER_BALANCE_SHORTAGE.getMessage());
    }

    @Test
    @DisplayName("매수 성공 - 새로운 주식")
    void buy_new_stock_with_enough_balance() throws Exception{
        // given
        when(userRepository.findByEmail("test")).thenReturn(Optional.ofNullable(user));
        when(userStockCommandRepository.findByUserIdAndStockCode(1L, "카카오"))
                .thenReturn(Optional.empty());
        // 실제 서비스 코드의 값 반환
        when(userStockCommandRepository.save(any(UserStock.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        userStockCommandService.buy(tradingRequest, "test");

        // then
        verify(userStockCommandRepository).save(any(UserStock.class));

        assertThat(user.getCashBalance()).isEqualTo(90000L);
        assertThat(user.getStockValuation()).isEqualTo(110000L);
    }

    @Test
    @DisplayName("매수 성공 - 보유 주식")
    void buy_exists_stock_with_enough_balance() throws Exception{
        // given
        when(userRepository.findByEmail("test")).thenReturn(Optional.ofNullable(user));
        when(userStockCommandRepository.findByUserIdAndStockCode(1L, "카카오"))
                .thenReturn(Optional.ofNullable(existingUserStock));

        // when
        userStockCommandService.buy(tradingRequest, "test");

        // then
        assertThat(user.getCashBalance()).isEqualTo(90000L);
        assertThat(user.getStockValuation()).isEqualTo(110000L);
        assertThat(existingUserStock.getQuantity()).isEqualTo(30L);
        assertThat(existingUserStock.getTotalAmount()).isEqualTo(30000L);
    }

    @Test
    @DisplayName("매도 성공 - 일부 매도")
    void sell_success() throws Exception{
        // given
        when(userRepository.findByEmail("test")).thenReturn(Optional.ofNullable(user));
        when(userStockCommandRepository.findByUserIdAndStockCode(1L, "카카오"))
                .thenReturn(Optional.ofNullable(existingUserStock));

        // when
        userStockCommandService.sell(tradingRequest, "test");

        // then
        assertThat(existingUserStock.getQuantity()).isEqualTo(10L);
        assertThat(existingUserStock.getTotalAmount()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("매도 실패 - 보유하지 않은 주식")
    void sell_fail_not_found() throws Exception{
        // given
        when(userRepository.findByEmail("test")).thenReturn(Optional.ofNullable(user));

        // when, then
        assertThatThrownBy(() -> userStockCommandService.sell(tradingRequest, "test"))
                .isInstanceOf(CustomException.class)
                .hasMessage(UserStockErrorCode.USER_STOCK_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("매도 실패 - 수량 부족")
    void sell_fail_not_enough_quantity() throws Exception{
        // given
        when(userRepository.findByEmail("test")).thenReturn(Optional.ofNullable(user));
        when(userStockCommandRepository.findByUserIdAndStockCode(1L, "카카오"))
                .thenReturn(Optional.ofNullable(existingUserStock));

        StockTradingRequest sellRequest = createStockTradingRequest("카카오", 5L, 25L);

        // when, then
        assertThatThrownBy(() -> userStockCommandService.sell(sellRequest, "test"))
                .isInstanceOf(CustomException.class)
                .hasMessage(UserStockErrorCode.USER_STOCK_STOCK_QUANTITY_EXCEED.getMessage());

    }

    private StockTradingRequest createStockTradingRequest(String stockCode, Long stockQuote, Long quantity) {
        return new StockTradingRequest() {
            public String getStockCode() { return stockCode; }
            public Long getStockQuote() { return stockQuote; }
            public Long getQuantity() { return quantity; }
        };
    }

    private UserStock createNewUserStock(User user, StockTradingRequest request) {
        return UserStock.builder()
                .user(user)
                .stockCode(request.getStockCode())
                .quantity(0L)
                .totalAmount(0L)
                .build();
    }
}