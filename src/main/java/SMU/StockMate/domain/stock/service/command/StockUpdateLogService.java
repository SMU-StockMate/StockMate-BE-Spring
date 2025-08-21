package SMU.StockMate.domain.stock.service.command;

import SMU.StockMate.domain.stock.exception.StockLogErrorCode;
import SMU.StockMate.domain.stock.repository.StockUpdateLogRepository;
import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.entity.enums.StockUpdateStatus;
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
    public boolean isUpdatedToday(LocalDate date) {
        return stockUpdateLogRepository.existsByUpdateDateAndStatus(date, StockUpdateStatus.SUCCESS);
    }

    @Transactional
    public StockUpdateLog createProgressLog(LocalDate today) {
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
