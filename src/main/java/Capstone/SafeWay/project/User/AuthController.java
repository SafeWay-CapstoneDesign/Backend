package Capstone.SafeWay.project.User;

import Capstone.SafeWay.project.Global.Security.Jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/jwt")
    public String getToken(@RequestParam Long userId){
        return jwtTokenProvider.token(userId);
    }
}
