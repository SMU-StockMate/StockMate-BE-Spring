package SMU.StockMate.domain.stock.service.query.v0;

import SMU.StockMate.domain.stock.converter.StockConverter;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.domain.stock.dto.StockDetailResponse;
import SMU.StockMate.domain.stock.dto.StockSearchListResponse;
import SMU.StockMate.domain.stock.dto.StocksInfoDto;
import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.service.query.StockInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockInfoServiceBasic implements StockInfoService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final StockRepository stockRepository;

    @Override
    public StockSearchListResponse search(String keyword, String cursor) {
        Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE + 1);
        List<Stock> stocks;

        if (cursor == null || cursor.isEmpty()) {
            stocks = stockRepository.findByNameContaining(keyword, pageable);
        } else {
            long cursorId = Long.parseLong(cursor);
            stocks = stockRepository.findByNameContainingNextPage(cursorId, keyword, pageable);
        }

        boolean hasMore = stocks.size() > DEFAULT_PAGE_SIZE;
        if (hasMore) {
            stocks.remove(stocks.size() - 1);
        }

        String nextCursor = hasMore ? stocks.get(stocks.size() - 1).getId().toString() : null;

        List<StocksInfoDto> stocksInfoDtos = stocks.stream()
                .map(StockConverter::toStockInfoDto)
                .toList();

        return StockConverter
                .toStockInfoResponse(stocksInfoDtos, nextCursor);
    }

    @Override
    public StockDetailResponse getStockDetail(String stockCode) {
        Stock stock = stockRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 종목 코드: " + stockCode));
        return StockDetailResponse.builder()
                .stockCode(stockCode)
                .standardCode(stock.getStandardCode())
                .koreanName(stock.getKoreanName())
                .basePrice(stock.getBasePrice())
                .build();
    }
}
