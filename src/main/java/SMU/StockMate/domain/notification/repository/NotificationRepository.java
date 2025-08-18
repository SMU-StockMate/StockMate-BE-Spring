package SMU.StockMate.domain.notification.repository;

import SMU.StockMate.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
