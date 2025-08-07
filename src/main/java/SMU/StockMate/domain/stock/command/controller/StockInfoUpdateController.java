package SMU.StockMate.domain.stock.command.controller;

import SMU.StockMate.domain.stock.command.service.StockInfoUpdateFacadeServiceImpl;
import SMU.StockMate.domain.stock.command.service.StockInfoUpdateService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoUpdateController {
    private final StockInfoUpdateFacadeServiceImpl stockInfoUpdateFacadeService;

    @PostMapping("/refresh")
    @Operation(method = "POST", summary = "주식 종목 업데이트", description = "코스피 및 코스닥 종목을 업데이트 해주는 API 입니다.")
    public CustomResponse<String> refreshStockInfo() {
        stockInfoUpdateFacadeService.refresh();
        return CustomResponse.onSuccess("종목 정보를 업데이트 하였습니다.");
    }
}
