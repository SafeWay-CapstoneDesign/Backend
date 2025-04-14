package Capstone.SafeWay.project.Auth;

import Capstone.SafeWay.project.Auth.Dto.LoginRequestDto;
import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "사용자 로그인",
            description = "이메일과 비밀번호로 로그인하고, 인증된 사용자의 JWT 토큰을 반환하는 API입니다.",
            tags = {"Auth"}
    )
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

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록하는 API입니다. 사용자 정보를 받아서 회원가입을 진행합니다.",
            tags = {"Auth"}
    )
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody BasicUserDto basicUserDto) {
        log.info("회원가입 요청: {}", basicUserDto.getEmail());
        String response = userService.createMember(basicUserDto);
        log.info("회원가입 완료: {}", basicUserDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "JWT 토큰 생성",
            description = "사용자 ID로 JWT 토큰을 생성하는 API입니다. ID를 받아서 JWT 토큰을 반환합니다.",
            tags = {"Auth"}
    )
    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }
}