package SMU.StockMate.domain.notification.controller;

import SMU.StockMate.domain.notification.dto.NotificationRequestDTO;
import SMU.StockMate.domain.notification.dto.PriceAlertRequestDTO;
import SMU.StockMate.domain.notification.service.NotificationService;
import SMU.StockMate.global.apiPayload.CustomResponse;
import SMU.StockMate.global.apiPayload.code.success.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    //알림 보내기
    @PostMapping
    public CustomResponse<Void> sendNotification(@RequestBody NotificationRequestDTO requestDTO) {
        notificationService.sendNotificationToAllTokens(
                requestDTO.getUserId(),
                requestDTO.getTitle(),
                requestDTO.getBody()
        );
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    // 목표 가격 설정
    @PostMapping("/target")
    public CustomResponse<Void> registerPriceAlert(@RequestBody PriceAlertRequestDTO dto) {
        notificationService.registerPriceAlert(dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    // 목표 가격에 도달 했는지 수동 체크할 수 있는 API

    @GetMapping("/alerts/check")
    public CustomResponse<Void> checkPriceAlerts() {
        notificationService.checkAndSendPriceAlerts();
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

}
