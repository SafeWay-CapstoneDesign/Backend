package Capstone.SafeWay.project.Auth;

import Capstone.SafeWay.project.Auth.Dto.LoginRequestDto;
import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.User.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.token(user.getId());

        return ResponseEntity.ok().body(token);
    }

    // 회원가입 (누구나 가능)
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BasicUserDto basicUserDto) {
        log.info("회원가입 요청: {}", basicUserDto.getEmail());
        String response = userService.createMember(basicUserDto);
        log.info("회원가입 완료: {}", basicUserDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }
}
