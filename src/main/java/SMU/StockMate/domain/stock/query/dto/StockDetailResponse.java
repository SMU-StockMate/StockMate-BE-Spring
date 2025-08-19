package SMU.StockMate.domain.stock.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockDetailResponse {
    private String stockCode;
    private String standardCode;
    private String koreanName;
    private int basePrice;

    // 보여줄 정보 더 추가
}
