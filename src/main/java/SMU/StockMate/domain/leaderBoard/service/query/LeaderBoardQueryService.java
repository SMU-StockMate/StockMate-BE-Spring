package SMU.StockMate.domain.leaderBoard.service.query;

import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardDTO;
import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardUserDTO;

import java.time.LocalDate;
import java.util.List;

public interface LeaderBoardQueryService {

    List<LeaderBoardDTO> getTop100ByDate(LocalDate date);

    LeaderBoardUserDTO getUserInLeaderBoard(Long userId);
}
