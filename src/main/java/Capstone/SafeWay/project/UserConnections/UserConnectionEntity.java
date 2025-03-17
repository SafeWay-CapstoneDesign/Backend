package Capstone.SafeWay.project.UserConnections;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_connections")
public class UserConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long guardianId; // 보호자 고유번호

    @Column(nullable = false)
    private Long starId; // 사용자 고유번호

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp connectedAt; // 연결 일자

    @PrePersist
    protected void onCreate() {
        this.connectedAt = new Timestamp(System.currentTimeMillis());
    }

    public UserConnectionEntity(Long guardianId, Long starId) {
        this.guardianId = guardianId;
        this.starId = starId;
    }
}
