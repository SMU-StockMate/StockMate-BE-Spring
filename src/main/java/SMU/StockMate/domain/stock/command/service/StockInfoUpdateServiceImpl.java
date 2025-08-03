package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.converter.StockConverter;
import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.command.repository.StockRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockInfoUpdateServiceImpl implements StockInfoUpdateService{
    private final StockCodeService stockCodeService;
    private final StockConverter stockConverter;
    private final StockRepository stockRepository;

    /**
     * 기존 테이블의 데이터 전체 삭제 후 업데이트
     */
    @Override
    public void update() {
        List<StockCodeDto> stockCodeDtos = stockCodeService.retrieveKospiMst();

        List<Stock> stocks = stockCodeDtos.stream()
                .map(stockConverter::toStock)
                .toList();

        stockRepository.deleteAll();
        stockRepository.saveAll(stocks);
    }
}
