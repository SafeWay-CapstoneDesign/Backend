package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import Capstone.SafeWay.project.UserConnections.Dto.GuardianCheckResponseDto;
import Capstone.SafeWay.project.UserConnections.Dto.UserConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserConnectionService {
    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;

    public void createConnection(String guardianEmail, Long starId) {
        UserEntity guardian = userRepository.findByEmail(guardianEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.GUARDIAN_NOT_FOUND));

        if (userConnectionRepository.existsByGuardianIdAndStarId(guardian.getId(), starId)) {
            throw new CustomException(ErrorCode.CONNECTION_ALREADY_EXISTS);
        }

        UserConnectionEntity connection = new UserConnectionEntity(guardian.getId(), starId);
        userConnectionRepository.save(connection);
    }

    public List<UserConnectionDto> getConnectionsByGuardian(Long guardianId) {
        return userConnectionRepository.findByGuardianId(guardianId).stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    public List<UserConnectionDto> getConnectionsByStar(Long starId) {
        return userConnectionRepository.findByStarId(starId).stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    public GuardianCheckResponseDto checkGuardianExists(String email) {
        UserEntity guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.GUARDIAN_NOT_FOUND));

        return new GuardianCheckResponseDto(true, guardian.getUsername(), guardian.getRole());
    }

    public List<UserConnectionDto> getAllConnections() {
        // 모든 보호자와 사용자의 연결을 조회
        return userConnectionRepository.findAll().stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    public List<UserConnectionDto> getAllConnectionsForStars() {
        // 모든 사용자에 대한 보호자 정보를 조회
        return userConnectionRepository.findAll().stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    public void deleteConnectionByUser(Long currentUserId) {
        Optional<UserConnectionEntity> connection = findConnectionByUser(currentUserId);

        if (connection.isEmpty()) {
            throw new CustomException(ErrorCode.CONNECTION_NOT_FOUND);
        }

        userConnectionRepository.delete(connection.get());
    }

    private Optional<UserConnectionEntity> findConnectionByUser(Long currentUserId) {
        return userConnectionRepository.findByGuardianId(currentUserId).stream().findFirst()
                .or(() -> userConnectionRepository.findByStarId(currentUserId).stream().findFirst());
    }
}
