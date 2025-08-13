package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.client.StockCodeClient;
import SMU.StockMate.domain.stock.command.converter.StockConverter;
import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.command.exception.StockLogErrorCode;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.entity.StockUpdateLog;
import SMU.StockMate.domain.stock.enums.StockUpdateStatus;
import SMU.StockMate.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInfoUpdateFacadeServiceImpl implements StockInfoUpdateFacadeService {
    private final StockInfoUpdateService stockInfoUpdateService;

    private final StockCodeClient stockCodeClient;
    private final StockConverter stockConverter;

    private final StockUpdateLogService stockUpdateLogService;

    /**
     * 불러온 주식 저장
     */
    @Override
    public void refresh() {
        LocalDate today = LocalDate.now();

        // 오늘 업데이트가 일어났는지 확인
        if (stockUpdateLogService.isUpdatedToday(today)) {
            log.info("오늘({})은 이미 주식 데이터가 업데이트 되었습니다.", today);
            return;
        }

        StockUpdateLog progressLog = stockUpdateLogService.createProgressLog(today);
        int updateCount = 0;
        try {
            int updatedCount = stockInfoUpdateService.refresh(getAllStocks());
            stockUpdateLogService.updateLogStatus(progressLog.getId(), updatedCount, StockUpdateStatus.SUCCESS);
        } catch (Exception e) {
            stockUpdateLogService.updateLogStatus(progressLog.getId(), updateCount, StockUpdateStatus.FAILED);
            throw new CustomException(StockLogErrorCode.LOG_UPDATE_FAILED);
        }
    }

    /**
     * kospi kosdac 주식 정보를 불러와서 합침
     */
    private List<Stock> getAllStocks() {
        CompletableFuture<List<StockCodeDto>> kospiFuture = CompletableFuture.supplyAsync(stockCodeClient::getKospiCode);
        CompletableFuture<List<StockCodeDto>> kosdacFuture = CompletableFuture.supplyAsync(stockCodeClient::getKosdacCode);

        return CompletableFuture.allOf(kospiFuture, kosdacFuture) // 모든 작업 수행 기다림
                .thenApply(voidResult -> { // 이후 작업
                    List<StockCodeDto> kospiCodes = kospiFuture.join();
                    List<StockCodeDto> kosdacCodes = kosdacFuture.join();

                    return Stream.of(kospiCodes, kosdacCodes)
                            .flatMap(Collection::stream)
                            .map(stockConverter::toStock)
                            .toList();
                }).join(); // 결과 대기

    }


}
