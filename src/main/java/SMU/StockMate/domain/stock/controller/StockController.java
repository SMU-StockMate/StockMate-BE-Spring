package SMU.StockMate.domain.stock.controller;

import SMU.StockMate.domain.stock.service.command.v0.StockUpdateFacadeServiceImpl;
import SMU.StockMate.domain.stock.dto.StockDetailResponse;
import SMU.StockMate.domain.stock.dto.StockPriceResponseDTO;
import SMU.StockMate.domain.stock.dto.StockSearchListResponse;
import SMU.StockMate.domain.stock.service.query.StockInfoService;
import SMU.StockMate.domain.stock.service.query.StockPriceService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class StockController {

    private final StockUpdateFacadeServiceImpl stockInfoUpdateFacadeService;
    private final StockInfoService stockInfoService;
    private final StockPriceService stockPriceService;

    @PostMapping("/refresh")
    @Operation(method = "POST", summary = "주식 종목 업데이트", description = "코스피 및 코스닥 종목을 업데이트 해주는 API 입니다.")
    public CustomResponse<String> refreshStockInfo() {
        stockInfoUpdateFacadeService.refresh();
        return CustomResponse.onSuccess("종목 정보를 업데이트 하였습니다.");
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "주식 종목 검색", description = "코스피 및 코스닥 종목을 검색하는 API 입니다.")
    public CustomResponse<StockSearchListResponse> searchStocks(
            @Parameter(description = "주식 종목을 검색하기 위한 종목 코드, 혹은 종목명을 적어주세요.")
            @RequestParam(value = "keyword") String keyword,

            @Parameter(description = "페이징을 위한 cursor 입니다.")
            @RequestParam(value = "cursor") String cursor
    ) {
        StockSearchListResponse searchedStocks = stockInfoService.search(keyword, cursor);
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

    @GetMapping("/{symbol}/price")
    @Operation(method = "GET", summary = "주식 가격 불러오기", description = "해당 종목의 가격 정보를 불러오는 API 입니다.")
    public CustomResponse<StockPriceResponseDTO> getPrice(@PathVariable String symbol) {
        var result = stockPriceService.getPrice(symbol.trim());
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, result);

    }
}
