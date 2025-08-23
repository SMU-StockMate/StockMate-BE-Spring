package SMU.StockMate.domain.userStock.controller;

import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import SMU.StockMate.domain.userStock.dto.StockTradingRequest;
import SMU.StockMate.domain.userStock.service.command.UserStockService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class UserStockController {
    private final UserStockService userStockService;

    @PostMapping("/buy")
    public CustomResponse<String> buyStocks(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody StockTradingRequest request) {
        userStockService.buy(request, customUserDetails.getUsername());
        return CustomResponse.onSuccess("주식을 매수하였습니다.");
    }

    @PostMapping("/sell")
    public CustomResponse<String> sellStocks(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody StockTradingRequest request) {
        userStockService.sell(request, customUserDetails.getUsername());
        return CustomResponse.onSuccess("주식을 매도하였습니다.");
    }
}
