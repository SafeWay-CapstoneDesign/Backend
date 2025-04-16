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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user-connections")
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
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        userConnectionService.createConnection(request.getGuardianEmail(), currentUser.getUser().getId());
        return ResponseEntity.ok(Map.of("message", "User-Guardian connection created successfully"));
    }

    @Operation(
            summary = "보호자의 모든 연결 조회",
            description = "보호자 ID를 기반으로 모든 연결을 조회하는 API입니다. 관리자(admin)는 모든 보호자의 연결을 조회할 수 있습니다.",
            tags = {"UserConnections"}
    )
    @GetMapping("/guardian")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByGuardian(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        // ADMIN or STAR 역할이 아니면 403 에러 발생
        if (currentUser.getUser().getRole().equals("ADMIN") || currentUser.getUser().getRole().equals("GUARDIAN")) {
            log.error("Unauthorized access: {}", currentUser.getUser().getRole());
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        // Guardian role 확인 후 보호자 연결 조회
        List<UserConnectionDto> connections = userConnectionService.getConnectionsByGuardian(currentUser.getUser().getId());
        return ResponseEntity.ok(connections);
    }

    @Operation(
            summary = "사용자의 보호자 조회",
            description = "사용자 ID를 기반으로 보호자 정보를 조회하는 API입니다. 관리자(admin)는 모든 사용자의 보호자 정보를 조회할 수 있습니다.",
            tags = {"UserConnections"}
    )
    @GetMapping("/star")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByStar(
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        // ADMIN or STAR 역할이 아니면 403 에러 발생
        if (currentUser.getUser().getRole().equals("ADMIN") || currentUser.getUser().getRole().equals("STAR")) {
            log.error("Unauthorized access: {}", currentUser.getUser().getRole());
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        // Star role 확인 후 사용자 연결 조회
        List<UserConnectionDto> connections = userConnectionService.getConnectionsByStar(currentUser.getUser().getId());
        return ResponseEntity.ok(connections);
    }

    @Operation(
            summary = "보호자 존재 여부 확인",
            description = "주어진 보호자 이메일로 보호자가 존재하는지 확인하는 API입니다.",
            tags = {"UserConnections"}
    )
    @GetMapping("/guardian/check")
    public ResponseEntity<GuardianCheckResponseDto> checkGuardianExists(@RequestParam String email) {
        return ResponseEntity.ok(userConnectionService.checkGuardianExists(email));
    }

    @Operation(
            summary = "연결 삭제",
            description = "현재 로그인된 사용자의 모든 보호자 연결을 삭제하는 API입니다.",
            tags = {"UserConnections"}
    )
    @DeleteMapping
    public ResponseEntity<String> deleteConnection(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        userConnectionService.deleteConnectionByUser(currentUser.getUser().getId());
        return ResponseEntity.ok("연결이 삭제되었습니다.");
    }
}
