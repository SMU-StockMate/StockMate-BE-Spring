package SMU.StockMate.domain.userStock.dto;

import lombok.Getter;

@Getter
public class StockTradingRequest {
    private String stockCode;

    private Long stockQuote; // 주식 호가

    private Long quantity;
}
