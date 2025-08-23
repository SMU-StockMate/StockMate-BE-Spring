package SMU.StockMate.domain.user.service;

import SMU.StockMate.domain.user.dto.PortfolioResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface PortfolioCommandService {
    PortfolioResponseDTO getPortfolio(UserDetails userDetails);
}
