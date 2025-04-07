package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
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

    public String createConnection(String guardianEmail, Long starId) {
        UserEntity guardian = userRepository.findByEmail(guardianEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.GUARDIAN_NOT_FOUND));

        boolean exists = userConnectionRepository.existsByGuardianIdAndStarId(guardian.getId(), starId);
        if (exists) {
            throw new CustomException(ErrorCode.CONNECTION_ALREADY_EXISTS);
        }

        UserConnectionEntity connection = new UserConnectionEntity(guardian.getId(), starId);
        userConnectionRepository.save(connection);
        return "유저 연결 완료";
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

    public UserConnectionDto getConnectionsByGuardianEmail(String email) {
        UserEntity guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.GUARDIAN_NOT_FOUND));

        UserConnectionEntity connection = userConnectionRepository.findByGuardianId(guardian.getId())
                .stream().findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NO_CONNECTION_FOR_GUARDIAN));

        return new UserConnectionDto(connection.getId(), connection.getGuardianId(), connection.getStarId(), connection.getConnectedAt());
    }

    public String deleteConnectionByUser(Long currentUserId) {
        Optional<UserConnectionEntity> connection = userConnectionRepository
                .findByGuardianId(currentUserId).stream().findFirst()
                .or(() -> userConnectionRepository.findByStarId(currentUserId).stream().findFirst());

        if (connection.isEmpty()) {
            throw new CustomException(ErrorCode.CONNECTION_NOT_FOUND);
        }

        UserConnectionEntity connectionEntity = connection.get();
        Long otherUserId = connectionEntity.getGuardianId().equals(currentUserId) ?
                connectionEntity.getStarId() : connectionEntity.getGuardianId();

        userRepository.findById(otherUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userConnectionRepository.delete(connectionEntity);

        return "유저 연결 삭제 완료";
    }
}
