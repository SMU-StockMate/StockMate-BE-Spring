package SMU.StockMate.domain.user.controller;

import SMU.StockMate.domain.auth.userDetails.CustomUserDetails;
import SMU.StockMate.domain.user.dto.PortfolioResponseDTO;
import SMU.StockMate.domain.user.service.PortfolioCommandService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포트폴리오 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioCommandService portfolioCommandService;

    @GetMapping("")
    @Operation(summary = "프로필 기본 조회")
    public CustomResponse<PortfolioResponseDTO> getPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {

        PortfolioResponseDTO dto = portfolioCommandService.getPortfolio(userDetails);

        return CustomResponse.onSuccess(dto);
    }
}
