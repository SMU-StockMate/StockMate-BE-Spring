package SMU.StockMate.domain.stock.query.controller;

import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;
import SMU.StockMate.domain.stock.query.service.StockSearchService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock/search")
public class StockInfoSearchController {

    private final StockSearchService stockSearchService;

    @GetMapping
    public CustomResponse<StockInfoResponse> searchStocks(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "cursor") String cursor
    ) {
        StockInfoResponse searchedStocks = stockSearchService.search(keyword, cursor);
        return CustomResponse.onSuccess(searchedStocks);
    }
}
