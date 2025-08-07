package SMU.StockMate.global.kis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TokenResponseDto(
        String accessToken,
        String accessTokenTokenExpired,
        String tokenType,
        Integer expiresIn
) {}
