package SMU.StockMate.domain.leaderBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserDailyStatsBatchService {

    private final JdbcTemplate jdbc;

    @Transactional
    public int buildFor(LocalDate date) {
        jdbc.update("DELETE FROM `user_daily_stats` WHERE `date` = ?",
                ps -> ps.setDate(1, java.sql.Date.valueOf(date)));

        String sql = """
        INSERT INTO `user_daily_stats` (`date`, `user_id`, `total_asset`, `total_returns`)
        SELECT
            ? AS stat_date,
            u.`user_id`,
            (COALESCE(MAX(u.`cash_balance`), 0)
               + COALESCE(SUM(us.`quantity` * s.`base_price`), 0))                  AS total_asset,
            CASE
                WHEN COALESCE(SUM(us.`total_amount`), 0) > 0 THEN
                    (
                      (COALESCE(MAX(u.`cash_balance`), 0) + COALESCE(SUM(us.`quantity` * s.`base_price`), 0))
                      - COALESCE(SUM(us.`total_amount`), 0)
                    ) / COALESCE(SUM(us.`total_amount`), 0)
                ELSE 0
            END
                AS total_returns
        FROM `users` u
        LEFT JOIN `user_stock` us ON us.`user_id` = u.`user_id`
        LEFT JOIN `stock` s       ON s.`id` = us.`stock_id`
        GROUP BY u.`user_id`;
        """;

        return jdbc.update(sql, ps -> ps.setDate(1, java.sql.Date.valueOf(date)));
    }

    @Transactional
    public int buildRange(LocalDate from, LocalDate toInclusive) {
        int total = 0;

        for (LocalDate d = from; !d.isAfter(toInclusive); d = d.plusDays(1)) {
            total += buildFor(d);
        }
        return total;
    }
}
