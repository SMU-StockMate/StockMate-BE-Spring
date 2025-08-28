package SMU.StockMate.domain.leaderBoard.entity;

import java.math.BigDecimal;

public interface LeaderBoardCandidate {
    Long getUserId();
    String getNickname();
    BigDecimal getTotalReturns();
    Long getTotalAsset();
}