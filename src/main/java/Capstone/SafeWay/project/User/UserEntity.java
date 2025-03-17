package Capstone.SafeWay.project.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(length = 20)
    private String phone;

    @Column(length = 50)
    private String role;

    public UserEntity(String email, String encode, String username, String phone, String role) {
        this.email = email;
        this.password = encode;
        this.username = username;
        this.phone = phone;
        this.role = role;
    }

    public void updateUser(String email, String password, String username,String phone, String role) {
        this.email = email;
        if (password != null && !password.isEmpty()) { // 비밀번호가 있을 때만 업데이트
            this.password = password;
        }
        this.username = username;
        this.phone = phone;
        this.role = role;
    }
}
