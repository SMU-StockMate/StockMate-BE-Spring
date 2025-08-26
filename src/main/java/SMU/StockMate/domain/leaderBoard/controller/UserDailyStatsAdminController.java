package SMU.StockMate.domain.leaderBoard.controller;

import SMU.StockMate.domain.leaderBoard.service.UserDailyStatsBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class UserDailyStatsAdminController {

    private final UserDailyStatsBatchService statsBatchService;

    // 단일 일자 재집계: /admin/stats/rebuild?date=2025-08-25
    @PostMapping("/rebuild")
    public String rebuild(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int rows = statsBatchService.buildFor(date);
        return "user_daily_stats rebuilt for " + date + " : " + rows + " rows";
    }

    // 구간 백필: /admin/stats/rebuild-range?from=2025-08-01&to=2025-08-25
    @PostMapping("/rebuild-range")
    public String rebuildRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        int rows = statsBatchService.buildRange(from, to);
        return "user_daily_stats rebuilt from " + from + " to " + to + " : " + rows + " rows total";
    }
}

