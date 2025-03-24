package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.UserConnections.Dto.CreateConnectionDto;
import Capstone.SafeWay.project.UserConnections.Dto.GuardianCheckResponseDto;
import Capstone.SafeWay.project.UserConnections.Dto.UserConnectionDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user-connections")
@RequiredArgsConstructor
public class UserConnectionController {
    private final UserConnectionService userConnectionService;
    private final UserRepository userRepository;
    private final UserConnectionRepository userConnectionRepository;

    // 보호자-사용자 연결 생성 (오류 수정 및 로그 추가)
    @PostMapping
    public ResponseEntity<Map<String, String>> createConnection(
            @RequestBody CreateConnectionDto request,
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        log.info("보호자-사용자 연결 요청: {}", request.getGuardianEmail());

        // `@AuthenticationPrincipal`이 `null`이면 직접 가져옴
        if (currentUser == null) {
            currentUser = getAuthenticatedUser();
        }

        log.info("현재 로그인된 사용자 ID: {}", currentUser.getUser().getId());

        userConnectionService.createConnection(request.getGuardianEmail(), currentUser.getUser().getId());

        log.info("연결 생성 완료 - 보호자 이메일: {}, 사용자 ID: {}", request.getGuardianEmail(), currentUser.getUser().getId());

        return ResponseEntity.ok(Collections.singletonMap("message", "User-Guardian connection created successfully"));
    }

    // 현재 로그인된 사용자 가져오기
    private UserDetailsImpl getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Unauthorized: No user logged in");
            throw new RuntimeException("Unauthorized: No user logged in");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            log.error("Unexpected principal type: {}", principal.getClass().getSimpleName());
            throw new RuntimeException("Unexpected principal type: " + principal.getClass().getSimpleName());
        }
    }

    // 보호자의 모든 연결 조회
    @GetMapping("/guardian/{guardianId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByGuardian(@PathVariable Long guardianId) {
        log.info("보호자 ID {}의 연결 조회 요청", guardianId);
        return ResponseEntity.ok(userConnectionService.getConnectionsByGuardian(guardianId));
    }

    // 사용자의 보호자 조회
    @GetMapping("/star/{starId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByStar(@PathVariable Long starId) {
        log.info("사용자 ID {}의 보호자 조회 요청", starId);
        return ResponseEntity.ok(userConnectionService.getConnectionsByStar(starId));
    }

    // 보호자 이메일로 조회
    @GetMapping("/guardian/check")
    public ResponseEntity<GuardianCheckResponseDto> checkGuardianExists(@RequestParam String email) {
        log.info("보호자 존재 여부 확인 요청 - 이메일: {}", email);

        Optional<UserEntity> guardian = userRepository.findByEmail(email);

        if (guardian.isEmpty()) {
            log.info("보호자 이메일 {} 존재하지 않음", email);
            return ResponseEntity.ok(new GuardianCheckResponseDto(false, null, null));
        }

        UserEntity foundGuardian = guardian.get();
        log.info("보호자 확인됨 - 이름: {}, 역할: {}", foundGuardian.getUsername(), foundGuardian.getRole());

        return ResponseEntity.ok(new GuardianCheckResponseDto(true, foundGuardian.getUsername(), foundGuardian.getRole()));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteConnection() {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("연결 삭제 요청: 현재 사용자 ID = {}", currentUser.getUser().getId());

        userConnectionService.deleteConnectionByUser(currentUser.getUser().getId());

        return ResponseEntity.ok("연결이 삭제되었습니다.");
    }


}
