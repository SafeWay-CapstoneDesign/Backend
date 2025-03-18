package Capstone.SafeWay.project.User;
import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.Dto.DetailUserDto;
import Capstone.SafeWay.project.User.Dto.TokenResponseDto;
import Capstone.SafeWay.project.User.Dto.UserDetailsImpl;
import Capstone.SafeWay.project.User.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BasicUserDto basicUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createMember(basicUserDto));
    }

    
    @GetMapping
    public String checkUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("UserDetails: {}", userDetails.getUsername());
        return "성공ㅋ";
    }

    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }
}
