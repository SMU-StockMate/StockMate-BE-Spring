package SMU.StockMate.domain.stock.command.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockCodeDto {
    // 단축 코드
    private String shortCode;

    // 표준 코드
    private String standardCode;

    // 한글명
    private String koreanName;

    // 기준가(전날 종가)
    private int basePrice;

    public StockCodeDto(String shortCode, String standardCode, String koreanName, int basePrice) {
        this.shortCode = shortCode;
        this.standardCode = standardCode;
        this.koreanName = koreanName;
        this.basePrice = basePrice;
    }
}
