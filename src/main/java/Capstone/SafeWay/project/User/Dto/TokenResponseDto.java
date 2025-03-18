package Capstone.SafeWay.project.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "로그인 Response")
public class TokenResponseDto {

    @Schema(
            description = "토큰 타입",
            example = "Bearer"
    )
    private final String type;

    @Schema(
            description = "액세스 토큰",
            example = "eyJhbGciOiJIUzI1VCJ9.eyJzdWUiIsImlhdCI6MTcwNjNzA2MDc4MjAwfQ.1234abcd5678efgh9012ijkl3456mnop"
    )
    private final String accessToken;

    @Schema(
            description = "리프레시 토큰",
            example = "eyJhbGckpXVCJ9.eyJzdWIiOiJ1c2VyQGMDc2NDAwLCJleHAiOjE3MDcyODYwMDB9.ab8ijkl9012mnop3456"
    )
    private final String refreshToken;

    @Builder
    private TokenResponseDto(String accessToken, String refreshToken){
        this.type = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
