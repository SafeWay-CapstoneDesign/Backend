package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.UserConnections.Dto.CreateConnectionDto;
import Capstone.SafeWay.project.UserConnections.Dto.GuardianCheckResponseDto;
import Capstone.SafeWay.project.UserConnections.Dto.UserConnectionDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "보호자-사용자 연결 생성",
            description = "보호자와 사용자를 연결하는 API입니다. 보호자 이메일을 입력받아 연결을 생성합니다.",
            tags = {"UserConnections"}
    )
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

    @Operation(
            summary = "보호자의 모든 연결 조회",
            description = "보호자 ID를 기반으로 모든 연결을 조회하는 API입니다.",
            tags = {"UserConnections"}
    )
    @GetMapping("/guardian/{guardianId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByGuardian(@PathVariable Long guardianId) {
        log.info("보호자 ID {}의 연결 조회 요청", guardianId);
        return ResponseEntity.ok(userConnectionService.getConnectionsByGuardian(guardianId));
    }

    @Operation(
            summary = "사용자의 보호자 조회",
            description = "사용자 ID를 기반으로 보호자 정보를 조회하는 API입니다.",
            tags = {"UserConnections"}
    )
    @GetMapping("/star/{starId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByStar(@PathVariable Long starId) {
        log.info("사용자 ID {}의 보호자 조회 요청", starId);
        return ResponseEntity.ok(userConnectionService.getConnectionsByStar(starId));
    }

    @Operation(
            summary = "보호자 존재 여부 확인",
            description = "주어진 보호자 이메일로 보호자가 존재하는지 확인하는 API입니다.",
            tags = {"UserConnections"}
    )
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

    @Operation(
            summary = "연결 삭제",
            description = "현재 로그인된 사용자의 모든 보호자 연결을 삭제하는 API입니다.",
            tags = {"UserConnections"}
    )
    @DeleteMapping
    public ResponseEntity<String> deleteConnection() {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("연결 삭제 요청: 현재 사용자 ID = {}", currentUser.getUser().getId());

        userConnectionService.deleteConnectionByUser(currentUser.getUser().getId());

        return ResponseEntity.ok("연결이 삭제되었습니다.");
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

}