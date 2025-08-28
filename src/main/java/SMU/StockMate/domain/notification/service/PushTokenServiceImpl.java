package SMU.StockMate.domain.notification.service;

import SMU.StockMate.domain.notification.dto.PushTokenRequestDTO;
import SMU.StockMate.domain.notification.entity.PushToken;
import SMU.StockMate.domain.notification.repository.PushTokenRepository;
import SMU.StockMate.domain.user.entity.User;
import SMU.StockMate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PushTokenServiceImpl implements PushTokenService {

    private final PushTokenRepository pushTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void registerToken(PushTokenRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        pushTokenRepository.findByUserAndDeviceId(user, dto.getDeviceId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setToken(dto.getToken());
                            existing.setActive(true);
                        },
                        () -> {
                            PushToken token = PushToken.builder()
                                    .user(user)
                                    .deviceId(dto.getDeviceId())
                                    .token(dto.getToken())
                                    .active(true)
                                    .build();
                            pushTokenRepository.save(token);
                        }
                );
    }
}
