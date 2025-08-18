package SMU.StockMate.domain.notification.controller;


import SMU.StockMate.domain.notification.dto.PushTokenRequestDTO;
import SMU.StockMate.domain.notification.service.PushTokenService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class PushTokenController {

    private final PushTokenService pushTokenService;

    // 기기별 토큰 등록
    @PostMapping("/tokens")
    public CustomResponse<Void> registerToken(@RequestBody PushTokenRequestDTO dto) {
        pushTokenService.registerToken(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
