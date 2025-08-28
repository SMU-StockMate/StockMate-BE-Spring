package SMU.StockMate.domain.notification.repository;

import SMU.StockMate.domain.notification.entity.PushToken;
import SMU.StockMate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
    Optional<PushToken> findByUserAndDeviceId(User user, String deviceId);
    List<PushToken> findAllByUserAndActiveTrue(User user);
}
