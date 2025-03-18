package Capstone.SafeWay.project.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserDto {
    private String username;
    private String email;
    private String password;
    private String phone;
    private String role;
}