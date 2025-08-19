package SMU.StockMate.domain.stock.query.controller;

import SMU.StockMate.domain.stock.query.dto.StockDetailResponse;
import SMU.StockMate.domain.stock.query.dto.StockInfoResponse;
import SMU.StockMate.domain.stock.query.service.StockInfoService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockInfoController {

    private final StockInfoService stockInfoService;

    @GetMapping("/search")
    @Operation(method = "GET", summary = "주식 종목 검색", description = "코스피 및 코스닥 종목을 검색하는 API 입니다.")
    public CustomResponse<StockInfoResponse> searchStocks(
            @Parameter(description = "주식 종목을 검색하기 위한 종목 코드, 혹은 종목명을 적어주세요.")
            @RequestParam(value = "keyword") String keyword,

            @Parameter(description = "페이징을 위한 cursor 입니다.")
            @RequestParam(value = "cursor") String cursor
    ) {
        StockInfoResponse searchedStocks = stockInfoService.search(keyword, cursor);
        return CustomResponse.onSuccess(searchedStocks);
    }

    @GetMapping()
    @Operation(method = "GET", summary = "주식 정보 불러오기", description = "해당 종목의 정보를 불러오는 API 입니다.")
    public CustomResponse<StockDetailResponse> getStockInfo(
            @Parameter(description = "주식 종목을 검색하기 위한 PathVariable")
            @RequestParam(value = "stockCode") String stockCode
    ) {
        StockDetailResponse stockDetailResponse = stockInfoService.getStockDetail(stockCode);
        return CustomResponse.onSuccess(stockDetailResponse);
    }
}
