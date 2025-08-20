package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.repository.StockCommandRepository;
import SMU.StockMate.domain.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockInfoUpdateServiceChangeActivation implements StockInfoUpdateService {
    private final StockCommandRepository stockRepository;

    /**
     * 새로 가져온 주식이 기본 테이블에 존재하면 유지
     * 존재하지 않으면 추가
     * 기존 주식이 새로 가져온 주식에 존재하지 않으면 is_Activation -> false 로 변경
     */
    @Override
    public int refresh(List<Stock> stocks) {
        int updateCount = 0;
        // 1. 기존에 존재하는 모든 주식을 가져옴
        List<Stock> existingStocks = stockRepository.findAll();

        // 2. 기존 주식 중 새 목록에 없는 것들 찾아서 비활성화
        for (Stock existingStock : existingStocks) {
            boolean isFound = false;

            for (Stock stock : stocks) {
                if (existingStock.getStockCode().equals(stock.getStockCode())) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound && existingStock.isActivate()) {
                existingStock.deActivate();
                updateCount++;
            }
        }

        // 3. 새로운 주식 처리
        for (Stock stock : stocks) {
            Stock existingStock = stockRepository.findByStockCode(stock.getStockCode())
                    .orElse(null);

            if (existingStock != null) {
                existingStock.reActivate();
                updateCount++;
            } else  {
                stockRepository.save(stock);
                updateCount++;
            }
        }

        return updateCount;
    }
}
