package SMU.StockMate.domain.leaderBoard.service;

import SMU.StockMate.domain.leaderBoard.entity.LeaderBoard;
import SMU.StockMate.domain.leaderBoard.entity.LeaderBoardCandidate;
import SMU.StockMate.domain.leaderBoard.repository.LeaderBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderBoardBatchService {

    private final LeaderBoardRepository repo;

    @Transactional
    public int rebuildTopByReturn(LocalDate date) {
        // 1) 해당 날짜 기존 랭킹 제거
        repo.deleteByDate(date);

        // 2) 후보 조회 (user_daily_stats)
        var all = repo.findCandidates(date);
        if (all.isEmpty()) return 0;

        // 3) 수익률 DESC, 자산 DESC, userId ASC
        var top = all.stream()
                .sorted(Comparator
                        .comparing(LeaderBoardCandidate::getTotalReturns,
                                Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(LeaderBoardCandidate::getTotalAsset,
                                Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(LeaderBoardCandidate::getUserId,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(100)
                .toList();

        // 4) 1..N등 부여 후 저장
        List<LeaderBoard> rows = new ArrayList<>(top.size());

        for (int i = 0; i < top.size(); i++) {
            var c = top.get(i);
            rows.add(LeaderBoard.builder()
                    .date(date)
                    .rank(i + 1)
                    .userId(c.getUserId())
                    .nickname(c.getNickname())
                    .totalAsset(c.getTotalAsset())
                    .totalReturns(c.getTotalReturns())
                    .build());
        }

        repo.saveAll(rows);
        return rows.size();
    }
}

