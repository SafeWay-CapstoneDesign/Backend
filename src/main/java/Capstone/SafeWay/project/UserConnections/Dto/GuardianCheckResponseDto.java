package Capstone.SafeWay.project.UserConnections.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuardianCheckResponseDto {
    private boolean exists;
    private String username;
    private String role;
}
