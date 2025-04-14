package Capstone.SafeWay.project.Notice;

import Capstone.SafeWay.project.Notice.Dto.FcmSendRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final FcmService fcmService;

    @Operation(
            summary = "푸시 알림 전송",
            description = "FCM을 사용하여 푸시 알림을 전송하는 API입니다. 알림 메시지와 함께 수신자 정보를 전달하여 알림을 보낼 수 있습니다. (아직 사용 x)",
            tags = {"FCM"}
    )
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody FcmSendRequestDto dto) {
        try {
            fcmService.sendPushAndSaveNotice(dto);
            return ResponseEntity.ok("알림 전송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전송 실패: " + e.getMessage());
        }
    }
}
