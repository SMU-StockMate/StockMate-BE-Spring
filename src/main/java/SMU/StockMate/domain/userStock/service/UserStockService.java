package SMU.StockMate.domain.userStock.service;

import SMU.StockMate.domain.userStock.dto.StockTradingRequest;

public interface UserStockService {
    /**
     * 요청하는 호가에 주식을 매수한다.
     * @param request
     * @param email
     */
    void buy(StockTradingRequest request, String email);

    /**
     * 요청 호가에 주식을 매도한다.
     * @param request
     * @param email
     */
    void sell(StockTradingRequest request, String email);
}
