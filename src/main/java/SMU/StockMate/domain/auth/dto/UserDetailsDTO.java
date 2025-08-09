package SMU.StockMate.domain.auth.dto;

import SMU.StockMate.domain.users.entity.User;
import lombok.Builder;

@Builder
public record UserDetailsDTO(
        String email,
        String password
) {
    public static UserDetailsDTO of(User user) {
        return new UserDetailsDTO(
                user.getEmail(),
                user.getPassword()
        );
    }
}
