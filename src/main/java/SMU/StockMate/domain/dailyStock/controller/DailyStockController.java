package SMU.StockMate.domain.dailyStock.controller;

import SMU.StockMate.domain.dailyStock.dto.DailyPriceRequest;
import SMU.StockMate.domain.dailyStock.dto.DailyPriceResponse;
import SMU.StockMate.domain.dailyStock.service.DailyStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stocks/daily")
@RequiredArgsConstructor
public class DailyStockController {
    private final DailyStockService service;

    @GetMapping("/daily")
    public Mono<DailyPriceResponse> daily(
            @RequestParam String symbol,                 // ex) 005930
            @RequestParam(defaultValue = "D") String period,
            @RequestParam(required = false) String from, // YYYYMMDD
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "true") boolean adjusted
    ) {
        return service.inquireDaily(new DailyPriceRequest(symbol, period, from, to, adjusted));
    }
}
