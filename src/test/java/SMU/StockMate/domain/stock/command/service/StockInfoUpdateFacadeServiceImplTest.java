package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.client.StockCodeClientImpl;
import SMU.StockMate.domain.stock.command.converter.StockConverter;
import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.entity.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockInfoUpdateFacadeServiceImplTest {
    @Mock
    StockInfoUpdateServiceImpl stockInfoUpdateService;

    @Mock
    StockCodeClientImpl stockCodeClient;

    @Mock
    StockConverter stockConverter;

    @InjectMocks
    StockInfoUpdateFacadeServiceImpl facadeService;

    @Test
    @DisplayName("코스피와 코스닥 데이터를 모두 업데이트 한다.")
    void refresh_all_stocks() throws Exception {
        // given
        List<StockCodeDto> kospiCodes = List.of(createStockCodeDto("111111", "카카오"));
        List<StockCodeDto> kosdacCodes = List.of(createStockCodeDto("222222", "잡주"));
        Stock testStock = createStock("333333", "테스트");

        when(stockCodeClient.getKospiCode()).thenReturn(kospiCodes);
        when(stockCodeClient.getKosdacCode()).thenReturn(kosdacCodes);
        when(stockConverter.toStock(any())).thenReturn(testStock);

        // when
        facadeService.refresh();

        // then
        verify(stockCodeClient).getKospiCode();
        verify(stockCodeClient).getKosdacCode();
        verify(stockInfoUpdateService).refresh(any());
    }

    private StockCodeDto createStockCodeDto(String shortCode, String koreanName) {
        return StockCodeDto.builder()
                .shortCode(shortCode)
                .standardCode("111")
                .koreanName(koreanName)
                .basePrice(1)
                .build();
    }

    private Stock createStock(String shortCode, String koreanName) {
        return Stock.builder()
                .stockCode(shortCode)
                .standardCode("111")
                .koreanName(koreanName)
                .basePrice(1)
                .build();
    }

}