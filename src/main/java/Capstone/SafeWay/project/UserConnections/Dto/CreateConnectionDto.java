package Capstone.SafeWay.project.UserConnections.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConnectionDto {
    private String guardianEmail; // 보호자 이메일 (프론트에서 입력)
}
