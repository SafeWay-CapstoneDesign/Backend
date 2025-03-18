package Capstone.SafeWay.project.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailUserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
}