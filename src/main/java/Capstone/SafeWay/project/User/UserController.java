package Capstone.SafeWay.project.User;
import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.Dto.DetailUserDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import Capstone.SafeWay.project.User.Dto.FcmTokenRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 내 정보 조회 (본인만 가능)
    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인된 사용자의 상세 정보를 조회하는 API입니다. 본인만 접근 가능합니다.",
            tags = {"User"}
    )
    @GetMapping
    public ResponseEntity<DetailUserDto> getUser() {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("현재 로그인된 사용자: {}", currentUser.getUsername());
        DetailUserDto userDto = userService.getUser(currentUser.getUser());
        return ResponseEntity.ok(userDto);
    }

    // 내 정보 수정 (본인만 가능)
    @Operation(
            summary = "내 정보 수정",
            description = "현재 로그인된 사용자의 정보를 수정하는 API입니다. 본인만 접근 가능합니다." +
                    "role은 관리자 ADMIN, 시각 장애인 STAR, 보호자 GARDIAN 세 형태 입니다.",
            tags = {"User"}
    )
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody BasicUserDto basicUserDto) {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("사용자 정보 수정 요청: {}", currentUser.getUsername());
        String response = userService.updateMember(basicUserDto, currentUser.getUser());
        log.info("사용자 정보 수정 완료: {}", currentUser.getUsername());
        return ResponseEntity.ok().body(response);
    }

    // 내 계정 삭제 (본인만 가능)
    @Operation(
            summary = "내 계정 삭제",
            description = "현재 로그인된 사용자의 계정을 삭제하는 API입니다. 본인만 접근 가능합니다.",
            tags = {"User"}
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("사용자 삭제 요청: {}", currentUser.getUsername());
        userService.deleteUser(currentUser.getUser());
        log.info("사용자 삭제 완료: {}", currentUser.getUsername());
        return ResponseEntity.noContent().build();
    }

    // 현재 로그인한 사용자 가져오기
    private UserDetailsImpl getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized: No user logged in");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            throw new RuntimeException("Unexpected principal type: " + principal.getClass().getSimpleName());
        }
    }

    @Operation(
            summary = "JWT 토큰 생성",
            description = "사용자 ID로 JWT 토큰을 생성하는 API입니다.",
            tags = {"User"}
    )
    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }

    @Operation(
            summary = "FCM 토큰 저장",
            description = "FCM 토큰을 저장하는 API입니다. 모바일 알림을 위해 사용됩니다. (아직 사용 x)",
            tags = {"User"}
    )
    @PostMapping("/token")
    public ResponseEntity<String> saveFcmToken(@RequestBody FcmTokenRequestDto dto) {
        userService.saveFcmToken(dto);
        return ResponseEntity.ok("토큰 저장 완료");
    }
}