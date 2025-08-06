package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.client.StockCodeClient;
import SMU.StockMate.domain.stock.command.converter.StockConverter;
import SMU.StockMate.domain.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StockInfoUpdateFacadeServiceImpl implements StockInfoUpdateFacadeService {
    private final StockInfoUpdateService stockInfoUpdateService;

    private final StockCodeClient stockCodeClient;
    private final StockConverter stockConverter;


    /**
     * 불러온 주식 저장
     */
    @Override
    public void refresh() {
        stockInfoUpdateService.refresh(getAllStocks());
    }

    /**
     * kospi kosdac 주식 정보를 불러와서 합침
     */
    private List<Stock> getAllStocks() {
        return Stream.of(
                        stockCodeClient.getKospiCode(),
                        stockCodeClient.getKosdacCode()
                ).flatMap(Collection::stream)
                .map(stockConverter::toStock)
                .toList();
    }
}
