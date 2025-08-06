package SMU.StockMate.domain.stock.command.client;

import SMU.StockMate.domain.stock.command.dto.StockCodeDto;

import java.util.List;

public interface StockCodeClient {

    // 코스피 종목의 정보를 받아온다
    List<StockCodeDto> getKospiCode();

    // 코스닥 정보를 받아온다
    List<StockCodeDto> getKosdacCode();
}
