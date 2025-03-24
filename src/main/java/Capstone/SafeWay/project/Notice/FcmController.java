package Capstone.SafeWay.project.Notice;

import Capstone.SafeWay.project.Notice.Dto.FcmSendRequestDto;
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
