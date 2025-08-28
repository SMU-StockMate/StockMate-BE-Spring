package SMU.StockMate.domain.leaderBoard.controller;

import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardDTO;
import SMU.StockMate.domain.leaderBoard.dto.LeaderBoardUserDTO;
import SMU.StockMate.domain.leaderBoard.service.LeaderBoardBatchService;
import SMU.StockMate.domain.leaderBoard.service.query.LeaderBoardQueryService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderBoardController {

    private final LeaderBoardQueryService leaderBoardQueryService;
    private final LeaderBoardBatchService batchService;

    // 날짜별 TOP100 (date=YYYY-MM-DD, 값이 없는 경우 -> 디폴트 값은 현재 날짜 기준으로 어제 값)
    @GetMapping("/top100")
    public CustomResponse<List<LeaderBoardDTO>> getTop100ByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LeaderBoardDTO> ranking =
                leaderBoardQueryService.getTop100ByDate(date != null ? date : LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));

        return CustomResponse.onSuccess(ranking);
    }

    @GetMapping("/top100/{userId}")
    public CustomResponse<LeaderBoardUserDTO> getUserInLeaderBoard(@PathVariable Long userId) {
        LeaderBoardUserDTO dto = leaderBoardQueryService.getUserInLeaderBoard(userId);

        return CustomResponse.onSuccess(dto);
    }

    // 자동 순위 생성에서 문제가 생긴 경우 - 수동으로 재생성
    @PostMapping("/rebuild")
    public CustomResponse<String> rebuild(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int count = batchService.rebuildTopByReturn(date);

        return CustomResponse.onSuccess("Rebuilt " + count + " rows for date " + date);
    }
}
