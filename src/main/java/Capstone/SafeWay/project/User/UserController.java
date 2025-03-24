package Capstone.SafeWay.project.User;
import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.Dto.DetailUserDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import Capstone.SafeWay.project.User.Dto.FcmTokenRequestDto;
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

    // 회원가입 (누구나 가능)
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BasicUserDto basicUserDto) {
        log.info("회원가입 요청: {}", basicUserDto.getEmail());
        String response = userService.createMember(basicUserDto);
        log.info("회원가입 완료: {}", basicUserDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 내 정보 조회 (본인만 가능)
    @GetMapping
    public ResponseEntity<DetailUserDto> getUser() {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("현재 로그인된 사용자: {}", currentUser.getUsername());
        DetailUserDto userDto = userService.getUser(currentUser.getUser());
        return ResponseEntity.ok(userDto);
    }

    // 내 정보 수정 (본인만 가능)
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody BasicUserDto basicUserDto) {
        UserDetailsImpl currentUser = getAuthenticatedUser();
        log.info("사용자 정보 수정 요청: {}", currentUser.getUsername());
        String response = userService.updateMember(basicUserDto, currentUser.getUser());
        log.info("사용자 정보 수정 완료: {}", currentUser.getUsername());
        return ResponseEntity.ok().body(response);
    }

    //  내 계정 삭제 (본인만 가능)
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

    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }

    @PostMapping("/token")
    public ResponseEntity<String> saveFcmToken(@RequestBody FcmTokenRequestDto dto) {
        userService.saveFcmToken(dto);
        return ResponseEntity.ok("토큰 저장 완료");
    }

}