package SMU.StockMate.domain.auth.dto;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken
) {}
