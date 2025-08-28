package SMU.StockMate.domain.leaderBoard.scheduler;

import SMU.StockMate.domain.leaderBoard.service.UserDailyStatsBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class UserDailyStatsScheduler {

    private final UserDailyStatsBatchService statsBatchService;

    // 매일 20:55 KST — 리더보드(21:00) 전에 집계 생성
    @Scheduled(cron = "0 55 20 * * *", zone = "Asia/Seoul")
    public void buildDailyStats() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int rows = statsBatchService.buildFor(target);
        System.out.println("[UserDailyStatsScheduler] " + target + " 집계: " + rows + "건");
    }
}