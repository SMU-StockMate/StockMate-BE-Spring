package SMU.StockMate.domain.stock.command.service;

import SMU.StockMate.domain.stock.command.dto.StockCodeDto;
import SMU.StockMate.domain.stock.entity.Stock;

import java.util.List;

public interface StockCodeService {

    // 코스피 종목의 정보를 받아온다
    public List<StockCodeDto> retrieveKospiMst();

//    public List<StockCodeDto> retrieveKosdacMst();
}
