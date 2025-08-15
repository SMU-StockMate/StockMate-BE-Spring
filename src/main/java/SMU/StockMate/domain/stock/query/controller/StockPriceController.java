package SMU.StockMate.domain.stock.query.controller;

import SMU.StockMate.domain.stock.query.dto.StockPriceResponseDTO;
import SMU.StockMate.domain.stock.query.service.StockPriceService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/stocks")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    @GetMapping("/{symbol}/price")
    public CustomResponse<StockPriceResponseDTO> getPrice(@PathVariable String symbol) {
        var result = stockPriceService.getPrice(symbol.trim());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);

    }

}