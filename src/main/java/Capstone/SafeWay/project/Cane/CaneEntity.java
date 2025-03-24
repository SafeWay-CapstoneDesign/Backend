package Capstone.SafeWay.project.Cane;

import Capstone.SafeWay.project.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cane")
public class CaneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String bluetoothId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime connectedAt;

    public void connectToUser(UserEntity user) {
        this.user = user;
        this.connectedAt = LocalDateTime.now();
    }
}
