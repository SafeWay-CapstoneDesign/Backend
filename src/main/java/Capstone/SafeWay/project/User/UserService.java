package Capstone.SafeWay.project.User;

import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.Dto.DetailUserDto;
import Capstone.SafeWay.project.User.Exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 추가

    // 회원 생성
    public String createMember(BasicUserDto basicUserDto) {
        UserEntity user = new UserEntity(
                basicUserDto.getEmail(),
                passwordEncoder.encode(basicUserDto.getPassword()), // 비밀번호 암호화 적용
                basicUserDto.getUsername(),
                basicUserDto.getPhone(),
                basicUserDto.getRole()
        );
        userRepository.save(user);
        return "User created successfully";
    }

    // 회원 정보 수정 (본인 또는 ADMIN만 가능)
    public String updateMember(BasicUserDto basicUserDto, UserEntity currentUser) {
        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 현재 사용자의 권한 확인 (본인 계정이거나, ADMIN이면 수정 가능)
        if (!user.getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("You do not have permission to update this user");
        }

        // 비밀번호가 변경될 경우 암호화 후 저장
        String encodedPassword = (basicUserDto.getPassword() != null && !basicUserDto.getPassword().isEmpty())
                ? passwordEncoder.encode(basicUserDto.getPassword())
                : user.getPassword(); // 기존 비밀번호 유지

        // 이메일, 전화번호, 역할 업데이트 (비밀번호 포함)
        user.updateUser(basicUserDto.getEmail(), encodedPassword, basicUserDto.getUsername(), basicUserDto.getPhone(), basicUserDto.getRole());

        userRepository.save(user);
        return "User updated successfully";
    }

    // 개별 사용자 조회 (본인 또는 ADMIN만 가능)
    public DetailUserDto getUser(UserEntity currentUser) {
        return new DetailUserDto(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getPhone(),
                currentUser.getRole()
        );
    }

    // 회원 삭제 (본인 계정 또는 관리자만 가능)
    public void deleteUser(UserEntity currentUser) {
        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new AccessDeniedException("You do not have permission to delete this user");
        }

        userRepository.deleteById(currentUser.getId());
    }

    // 관리자 권한 확인 메서드
    private boolean isAdmin(UserEntity user) {
        return "ADMIN".equals(user.getRole());
    }
}
