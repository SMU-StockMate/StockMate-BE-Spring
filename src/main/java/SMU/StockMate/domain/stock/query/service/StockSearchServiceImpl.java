package SMU.StockMate.domain.stock.query.service;

import SMU.StockMate.domain.stock.entity.Stock;
import SMU.StockMate.domain.stock.query.converter.StockQueryConverter;
import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;
import SMU.StockMate.domain.stock.query.dto.StocksInfoDto;
import SMU.StockMate.domain.stock.query.repository.StockQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StockSearchServiceImpl implements StockSearchService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final StockQueryRepository stockRepository;

    @Override
    public StockInfoResponse search(String keyword, String cursor) {
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
                .map(StockQueryConverter::toStockInfoDto)
                .toList();

        return StockQueryConverter
                .toStockInfoResponse(stocksInfoDtos, nextCursor);
    }
}
