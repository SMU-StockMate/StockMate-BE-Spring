package SMU.StockMate.domain.stock.service;

import SMU.StockMate.domain.stock.exception.StockLogErrorCode;
import SMU.StockMate.domain.stock.repository.StockUpdateLogRepository;
import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.entity.enums.StockUpdateStatus;
import SMU.StockMate.domain.stock.service.command.StockUpdateLogService;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockUpdateLogServiceTest {

    @Mock
    StockUpdateLogRepository stockUpdateLogRepository;

    @InjectMocks
    StockUpdateLogService stockUpdateLogService;

    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
    }

    @Test
    @DisplayName("오늘 SUCCESS 상태의 로그가 있으면 true 반환.")
    void isUpdated_true() throws Exception{
        // given
        when(stockUpdateLogRepository.existsByUpdateDateAndStatus(today, StockUpdateStatus.SUCCESS)).thenReturn(true);

        // when
        boolean result = stockUpdateLogService.isUpdatedToday(today);

        // then
        assertThat(result).isTrue();
        verify(stockUpdateLogRepository).existsByUpdateDateAndStatus(today, StockUpdateStatus.SUCCESS);
    }

    @Test
    @DisplayName("오늘 SUCCESS 상태의 로그가 없으면 false 반환.")
    void isUpdated_false() throws Exception{
        // given
        when(stockUpdateLogRepository.existsByUpdateDateAndStatus(today, StockUpdateStatus.SUCCESS)).thenReturn(false);

        // when
        boolean result = stockUpdateLogService.isUpdatedToday(today);

        // then
        assertThat(result).isFalse();
        verify(stockUpdateLogRepository).existsByUpdateDateAndStatus(today, StockUpdateStatus.SUCCESS);
    }

    @Test
    @DisplayName("IN_PROGRESS 상태의 로그를 성공적으로 생성한다.")
    void createProgressLog_success() throws Exception{
        // given
        StockUpdateLog expectedLog = StockUpdateLog.builder()
                .updateDate(today)
                .status(StockUpdateStatus.IN_PROGRESS)
                .build();
        when(stockUpdateLogRepository.save(any(StockUpdateLog.class))).thenReturn(expectedLog);

        // when
        StockUpdateLog result = stockUpdateLogService.createProgressLog(today);

        // then
        assertThat(result.getUpdateDate()).isEqualTo(expectedLog.getUpdateDate());
        assertThat(result.getStatus()).isEqualTo(StockUpdateStatus.IN_PROGRESS);
        verify(stockUpdateLogRepository).save(any(StockUpdateLog.class));
    }

    @Test
    @DisplayName("존재하는 로그의 상태를 성공적으로 업데이트 한다.")
    void updateLogStatus_success() throws Exception{
        // given
        Long logId = 1L;
        int updateCount = 100;
        StockUpdateStatus status = StockUpdateStatus.SUCCESS;

        StockUpdateLog mockLog = mock(StockUpdateLog.class);
        when(stockUpdateLogRepository.findById(logId))
                .thenReturn(Optional.ofNullable(mockLog));

        // when
        stockUpdateLogService.updateLogStatus(logId, updateCount, status);

        // then
        verify(stockUpdateLogRepository).findById(logId);
        verify(mockLog).updateStockLog(updateCount, status);
    }

    @Test
    @DisplayName("존재하지 않는 로그 ID로 상태 업데이트 시 CustomException 이 발생한다")
    void updateLogStatus_log_not_found_throws_exception() {
        // given
        Long nonExistentLogId = 999L;
        int updatedCount = 100;
        StockUpdateStatus newStatus = StockUpdateStatus.SUCCESS;

        when(stockUpdateLogRepository.findById(nonExistentLogId))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> stockUpdateLogService.updateLogStatus(nonExistentLogId, updatedCount, newStatus))
                .isInstanceOf(CustomException.class)
                .hasMessage(StockLogErrorCode.LOG_NOT_FOUND.getMessage());

        verify(stockUpdateLogRepository).findById(nonExistentLogId);
    }


}