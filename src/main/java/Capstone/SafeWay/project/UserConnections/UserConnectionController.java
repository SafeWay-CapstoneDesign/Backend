package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.User.UserService;
import Capstone.SafeWay.project.UserConnections.Dto.CreateConnectionDto;
import Capstone.SafeWay.project.UserConnections.Dto.GuardianCheckResponseDto;
import Capstone.SafeWay.project.UserConnections.Dto.UserConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user-connections")
@RequiredArgsConstructor
public class UserConnectionController {
    private final UserConnectionService userConnectionService;
    private final UserRepository userRepository;

    // 보호자-사용자 연결 생성
    @PostMapping
    public ResponseEntity<Map<String, String>> createConnection(@RequestBody CreateConnectionDto request, @AuthenticationPrincipal UserEntity currentUser) {
        userConnectionService.createConnection(request.getGuardianEmail(), currentUser.getId());
        return ResponseEntity.ok(Collections.singletonMap("message", "User-Guardian connection created successfully"));
    }

    // 보호자의 모든 연결 조회
    @GetMapping("/guardian/{guardianId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByGuardian(@PathVariable Long guardianId) {
        return ResponseEntity.ok(userConnectionService.getConnectionsByGuardian(guardianId));
    }

    // 사용자의 보호자 조회
    @GetMapping("/star/{starId}")
    public ResponseEntity<List<UserConnectionDto>> getConnectionsByStar(@PathVariable Long starId) {
        return ResponseEntity.ok(userConnectionService.getConnectionsByStar(starId));
    }

    // 보호자 이메일로 조회
    @GetMapping("/guardian/check")
    public ResponseEntity<GuardianCheckResponseDto> checkGuardianExists(@RequestParam String email) {
        Optional<UserEntity> guardian = userRepository.findByEmail(email);

        if (guardian.isEmpty()) {
            return ResponseEntity.ok(new GuardianCheckResponseDto(false, null, null));
        }

        UserEntity foundGuardian = guardian.get();
        return ResponseEntity.ok(new GuardianCheckResponseDto(true, foundGuardian.getUsername(), foundGuardian.getRole()));
    }


    // 연결 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        userConnectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
}
