package Capstone.SafeWay.project.Cane;

import Capstone.SafeWay.project.Cane.Dto.CaneRequestDto;
import Capstone.SafeWay.project.Cane.Dto.CaneResponseDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cane")
@RequiredArgsConstructor
public class CaneController {

    private final CaneService caneService;

    // 지팡이 등록
    @PostMapping("/register")
    public ResponseEntity<String> registerCane(@RequestBody CaneRequestDto dto) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("지팡이 등록 요청: userId={}, bluetoothId={}", currentUser.getUser().getId(), dto.getBluetoothId());
        String result = caneService.registerCane(dto, currentUser.getUser());
        log.info("지팡이 등록 완료: {}", dto.getBluetoothId());
        return ResponseEntity.ok(result);
    }

    // 내 지팡이 조회
    @GetMapping("/my")
    public ResponseEntity<List<CaneResponseDto>> getMyCanes() {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("내 지팡이 조회 요청: userId={}", currentUser.getUser().getId());
        List<CaneResponseDto> result = caneService.getMyCanes(currentUser.getUser());
        log.info("지팡이 {}개 조회됨", result.size());
        return ResponseEntity.ok(result);
    }

    // 지팡이 연결
    @PostMapping("/connect/{caneId}")
    public ResponseEntity<String> connectCane(@PathVariable Long caneId) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("지팡이 연결 요청: userId={}, caneId={}", currentUser.getUser().getId(), caneId);
        String result = caneService.connectCane(caneId, currentUser.getUser());
        log.info("지팡이 연결 완료: caneId={}", caneId);
        return ResponseEntity.ok(result);
    }

    // 지팡이 연결 해제
    @PostMapping("/disconnect/{caneId}")
    public ResponseEntity<String> disconnectCane(@PathVariable Long caneId) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("지팡이 연결 해제 요청: userId={}, caneId={}", currentUser.getUser().getId(), caneId);
        String result = caneService.disconnectCane(caneId, currentUser.getUser());
        log.info("지팡이 연결 해제 완료: caneId={}", caneId);
        return ResponseEntity.ok(result);
    }

    // 현재 로그인한 사용자 가져오기
    private UserDetailsImpl getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("로그인되지 않은 사용자입니다.");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            throw new RuntimeException("인증된 사용자 정보를 불러올 수 없습니다.");
        }
    }
}
