package SMU.StockMate.domain.stock.service;

import SMU.StockMate.domain.stock.client.StockCodeClientImpl;
import SMU.StockMate.domain.stock.converter.StockConverter;
import SMU.StockMate.domain.stock.dto.StockCodeDto;
import SMU.StockMate.domain.stock.exception.StockLogErrorCode;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.entity.enums.StockUpdateStatus;
import SMU.StockMate.domain.stock.service.command.v0.StockUpdateFacadeServiceImpl;
import SMU.StockMate.domain.stock.service.command.v0.StockUpdateServiceBasic;
import SMU.StockMate.domain.stock.service.command.StockUpdateLogService;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockUpdateFacadeServiceImplTest {
    @Mock
    StockUpdateServiceBasic stockInfoUpdateService;

    @Mock
    StockCodeClientImpl stockCodeClient;

    @Mock
    StockUpdateLogService stockUpdateLogService;

    @InjectMocks
    StockUpdateFacadeServiceImpl facadeService;

    private LocalDate today = LocalDate.now();

    @Test
    @DisplayName("코스피와 코스닥 데이터를 모두 업데이트 성공힌다.")
    void refresh_all_stocks_success() throws Exception {
        // given
        List<StockCodeDto> kospiCodes = List.of(createStockCodeDto("111111", "카카오"));
        List<StockCodeDto> kosdacCodes = List.of(createStockCodeDto("222222", "잡주"));
        Stock testStock = createStock("333333", "테스트");
        StockUpdateLog progressLog = createProgressLog(today);


        when(stockCodeClient.getKospiCode()).thenReturn(kospiCodes);
        when(stockCodeClient.getKosdacCode()).thenReturn(kosdacCodes);
        when(stockUpdateLogService.isUpdatedToday(today)).thenReturn(false);
        when(stockUpdateLogService.createProgressLog(today)).thenReturn(progressLog);

        // when
        facadeService.refresh();

        // then
        verify(stockCodeClient).getKospiCode();
        verify(stockCodeClient).getKosdacCode();
        verify(stockInfoUpdateService).refresh(any());
    }

    @Test
    @DisplayName("코스피와 코스닥 데이터를 모두 업데이트 실패힌다.")
    void refresh_all_stocks_fail() throws Exception {
        // given
        StockUpdateLog progressLog = createProgressLog(today);

        when(stockUpdateLogService.isUpdatedToday(today)).thenReturn(false);
        when(stockUpdateLogService.createProgressLog(today)).thenReturn(progressLog);
        when(stockInfoUpdateService.refresh(any())).thenThrow(new CustomException(StockLogErrorCode.LOG_UPDATE_FAILED));

        // when, then
        assertThatThrownBy(() -> facadeService.refresh())
                .isInstanceOf(CustomException.class)
                .hasMessage(StockLogErrorCode.LOG_UPDATE_FAILED.getMessage());
    }

    @Test
    @DisplayName("업데이트 과정에서 status 가 IN_PROGRESS -> SUCCESS 로 변환된다.")
    void status_change_success() throws Exception{
        // given
        StockUpdateLog progressLog = createProgressLog(today);
        List<StockCodeDto> kospiCodes = List.of(createStockCodeDto("111111", "카카오"));
        List<StockCodeDto> kosdacCodes = List.of(createStockCodeDto("222222", "잡주"));
        Stock testStock = createStock("333333", "테스트");

        when(stockCodeClient.getKospiCode()).thenReturn(List.of(createStockCodeDto("111111", "테스트")));
        when(stockCodeClient.getKosdacCode()).thenReturn(List.of());
        when(stockUpdateLogService.isUpdatedToday(today)).thenReturn(false);
        when(stockUpdateLogService.createProgressLog(today)).thenReturn(progressLog);

        // when
        facadeService.refresh();

        // then
        InOrder inOrder = Mockito.inOrder(stockUpdateLogService);

        inOrder.verify(stockUpdateLogService).createProgressLog(today);

        inOrder.verify(stockUpdateLogService).updateLogStatus(
                eq(progressLog.getId()),
                anyInt(),
                eq(StockUpdateStatus.SUCCESS)
        );
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

    protected StockUpdateLog createProgressLog(LocalDate today) {
        return StockUpdateLog.builder()
                .id(1L)
                .updateDate(today)
                .status(StockUpdateStatus.IN_PROGRESS)
                .build();
    }

}