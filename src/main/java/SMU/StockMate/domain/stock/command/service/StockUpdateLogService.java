package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.exception.StockLogErrorCode;
import SMU.StockMate.domain.stock.command.repository.StockUpdateLogRepository;
import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.enums.StockUpdateStatus;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class StockUpdateLogService {
    public final StockUpdateLogRepository stockUpdateLogRepository;


    /**
     * Repository Success 가 조회된 경우
     * @return true
     */
    @Transactional(readOnly = true)
    protected boolean isUpdatedToday(LocalDate date) {
        return stockUpdateLogRepository.existsByUpdateDateAndStatus(date, StockUpdateStatus.SUCCESS);
    }

    @Transactional
    protected StockUpdateLog createProgressLog(LocalDate today) {
        StockUpdateLog progressLog = StockUpdateLog.builder()
                .updateDate(today)
                .status(StockUpdateStatus.IN_PROGRESS)
                .build();
        return stockUpdateLogRepository.save(progressLog);
    }

    public void updateLogStatus(Long id, int updatedCount, StockUpdateStatus status) {
        StockUpdateLog stockUpdateLog = stockUpdateLogRepository.findById(id)
                .orElseThrow(() -> new CustomException(StockLogErrorCode.LOG_NOT_FOUND));
        stockUpdateLog.updateStockLog(updatedCount, status);
    }

}
