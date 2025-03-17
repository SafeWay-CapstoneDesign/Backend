package Capstone.SafeWay.project.UserConnections.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectionDto {
    private Long id;
    private Long guardianId;
    private Long starId;
    private Timestamp connectedAt;
}
