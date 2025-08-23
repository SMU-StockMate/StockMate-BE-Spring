package SMU.StockMate.domain.dailyStock.controller;

import SMU.StockMate.domain.dailyStock.dto.DailyStockResponseDTO;
import SMU.StockMate.domain.dailyStock.service.DailyStockService;
import SMU.StockMate.domain.stock.repository.StockRepository;
import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import SMU.StockMate.domain.stock.entity.Stock;

@RestController
@RequestMapping("/daily-stock")
@RequiredArgsConstructor
public class DailyStockController {

    private final DailyStockService dailyStockService;
    private final StockRepository stockRepository;

    // 최근 30일 조회
    @GetMapping("/30days")
    public CustomResponse<Map<String, Object>> getDailyStock(@RequestParam String stockCode) {
        List<DailyStockResponseDTO> list = dailyStockService.getLast30Days(stockCode);


        String symbol = stockCode;
        String name = stockRepository.findByStockCode(stockCode)
                .map(Stock::getKoreanName)
                .orElse("");


        List<Map<String, Object>> data = new ArrayList<>(list.size());
        for (DailyStockResponseDTO dto : list) {
            Map<String, Object> chart = new LinkedHashMap<>();
            chart.put("date", dto.getTradeDate().toString());
            chart.put("open", dto.getOpenPrice());
            chart.put("high", dto.getHighPrice());
            chart.put("low", dto.getLowPrice());
            chart.put("close", dto.getClosePrice());
            chart.put("volume", dto.getVolume());
            data.add(chart);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("symbol", symbol);
        result.put("name", name);
        result.put("data", data);


        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    // 범위 기반 조회
    @GetMapping("/range")
    public CustomResponse<Map<String, Object>> range(
            @RequestParam String stockCode,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        final List<DailyStockResponseDTO> list = dailyStockService.getDailyStock(stockCode, from, to);

        final String name = stockRepository.findByStockCode(stockCode)
                .map(Stock::getKoreanName)
                .orElse("");

        final List<Map<String, Object>> data = new ArrayList<>(list.size());

        for (DailyStockResponseDTO d : list) {
            final Map<String, Object> chart = new LinkedHashMap<>();
            chart.put("date", d.getTradeDate().toString());
            chart.put("open", d.getOpenPrice());
            chart.put("high", d.getHighPrice());
            chart.put("low", d.getLowPrice());
            chart.put("close", d.getClosePrice());
            chart.put("volume", d.getVolume());
            data.add(chart);
        }

        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("symbol", stockCode);
        result.put("name", name);
        result.put("data", data);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);
    }


}
