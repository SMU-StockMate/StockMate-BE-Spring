package SMU.StockMate.domain.stock.service.command.v0;

import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.service.command.StockUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Service
@RequiredArgsConstructor
@Transactional
public class StockUpdateServiceBasic implements StockUpdateService {
    private final StockRepository stockRepository;

    /**
     * 기존 테이블의 데이터 전체 삭제 후 업데이트
     */
    @Override
    public int refresh(List<Stock> stocks) {
        stockRepository.saveAll(stocks);
        return stocks.size();
    }
}
