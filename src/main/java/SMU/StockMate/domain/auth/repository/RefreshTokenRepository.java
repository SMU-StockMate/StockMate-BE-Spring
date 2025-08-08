package SMU.StockMate.domain.auth.repository;

import SMU.StockMate.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUsername(String username);
    void deleteByUsername(String username);
}
