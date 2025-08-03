package SMU.StockMate.domain.stock.command.controller;

import SMU.StockMate.domain.stock.command.service.StockInfoUpdateService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoUpdateController {
    private final StockInfoUpdateService stockInfoUpdateService;

    @PostMapping("/refresh")
    public CustomResponse<String> refreshStockInfo() {
        stockInfoUpdateService.update();
        return CustomResponse.onSuccess("종목 정보를 업데이트 하였습니다.");
    }
}
