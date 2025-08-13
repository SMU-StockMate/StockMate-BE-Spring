package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.repository.StockCommandRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StockInfoUpdateServiceImplTest {
    @Mock
    StockCommandRepository stockCommandRepository;

    @InjectMocks
    StockInfoUpdateServiceImpl stockInfoUpdateService;

    List<Stock> stocks;

    @BeforeEach
    void setUp() {
        stocks = List.of(
                Stock.builder()
                        .id(1L)
                        .stockCode("111111")
                        .koreanName("카카오")
                        .basePrice(1)
                        .build(),
                Stock.builder()
                        .id(2L)
                        .stockCode("222222")
                        .koreanName("삼성전자")
                        .basePrice(1)
                        .build()
        );
    }

    @Test
    @DisplayName("refresh시 성공적으로 주식 정보를 갱신한다.")
    void refresh_success() throws Exception{
        // given

        // when
        stockInfoUpdateService.refresh(stocks);

        // then
        verify(stockCommandRepository).saveAll(stocks);
    }
}