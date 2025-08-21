package SMU.StockMate.domain.stock.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockUpdateStatus {
    SUCCESS("SUCCESS", "성공"),
    FAILED("FAILED", "실패"),
    IN_PROGRESS("IN_PROGRESS", "진행중");

    private final String code;
    private final String description;
}
