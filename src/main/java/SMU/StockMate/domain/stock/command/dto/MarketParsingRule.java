package SMU.StockMate.domain.stock.command.dto;

import lombok.Getter;

@Getter
public enum MarketParsingRule {
    KOSPI(0, 9, 21, 228, 42, 51),
    KOSDAC(0, 9, 21, 222, 37, 46);

    private final int shortCodeStart;
    private final int shortCodeEnd;
    private final int standardCodeEnd;
    private final int backPartLength;
    private final int basePriceStart;
    private final int basePriceEnd;

    MarketParsingRule(int shortCodeStart, int shortCodeEnd, int standardCodeEnd,
                      int backPartLength, int basePriceStart, int basePriceEnd) {
        this.shortCodeStart = shortCodeStart;
        this.shortCodeEnd = shortCodeEnd;
        this.standardCodeEnd = standardCodeEnd;
        this.backPartLength = backPartLength;
        this.basePriceStart = basePriceStart;
        this.basePriceEnd = basePriceEnd;
    }
}
