package Capstone.SafeWay.project.User;

import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.User.Dto.BasicUserDto;
import Capstone.SafeWay.project.User.Dto.DetailUserDto;
import Capstone.SafeWay.project.User.Dto.FcmTokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 생성
    public String createMember(BasicUserDto basicUserDto) {
        if (userRepository.findByEmail(basicUserDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        UserEntity user = new UserEntity(
                basicUserDto.getEmail(),
                passwordEncoder.encode(basicUserDto.getPassword()),
                basicUserDto.getUsername(),
                basicUserDto.getPhone(),
                basicUserDto.getRole()
        );
        userRepository.save(user);
        return "유저 생성 완료";
    }

    // 회원 정보 수정 (본인 또는 ADMIN만 가능)
    public String updateMember(BasicUserDto basicUserDto, UserEntity currentUser) {
        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String encodedPassword = (basicUserDto.getPassword() != null && !basicUserDto.getPassword().isEmpty())
                ? passwordEncoder.encode(basicUserDto.getPassword())
                : user.getPassword();

        user.updateUser(
                basicUserDto.getEmail(),
                encodedPassword,
                basicUserDto.getUsername(),
                basicUserDto.getPhone(),
                basicUserDto.getRole()
        );

        userRepository.save(user);
        return "유저 수정 완료";
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

    // 회원 삭제
    public String deleteUser(UserEntity currentUser) {
        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(currentUser.getId()) && !isAdmin(currentUser)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        userRepository.deleteById(currentUser.getId());
        return "유저 삭제 완료";
    }

    // 관리자 권한 확인
    private boolean isAdmin(UserEntity user) {
        return "ADMIN".equals(user.getRole());
    }

    // FCM 토큰 저장
    public void saveFcmToken(FcmTokenRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateFcmToken(dto.getToken());
    }
}
