package SMU.StockMate.domain.leaderBoard.scheduler;

import SMU.StockMate.domain.leaderBoard.service.LeaderBoardBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class LeaderBoardScheduler {

    private final LeaderBoardBatchService batchService;

    // 매일 21:00 KST
    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Seoul")
    public void buildDaily() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int saved = batchService.rebuildTopByReturn(target);

        System.out.println("[LeaderBoardScheduler] " + target + " 저장: " + saved + "건");
    }
}