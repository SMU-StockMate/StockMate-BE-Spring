package SMU.StockMate.domain.leaderBoard.dto;

import java.math.BigDecimal;

public record LeaderBoardDTO (
        Long userId,
        String nickname,
        BigDecimal totalReturns,
        Long totalAsset
) {}
