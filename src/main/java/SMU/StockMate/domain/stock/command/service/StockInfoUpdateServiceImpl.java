package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.repository.StockCommandRepository;
import SMU.StockMate.domain.stock.command.repository.StockUpdateLogRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockInfoUpdateServiceImpl implements StockInfoUpdateService {
    private final StockCommandRepository stockRepository;
    private final StockUpdateLogRepository stockUpdateLogRepository;

    /**
     * 기존 테이블의 데이터 전체 삭제 후 업데이트
     */
    @Override
    public int refresh(List<Stock> stocks) {
        stockRepository.saveAll(stocks);
        return stocks.size();
    }



}
