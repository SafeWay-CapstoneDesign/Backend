package Capstone.SafeWay.project.UserConnections;

import Capstone.SafeWay.project.UserConnections.Dto.UserConnectionDto;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserConnectionService {
    private final UserConnectionRepository userConnectionRepository;
    private final UserRepository userRepository;

    // 보호자-사용자 연결 생성
    public void createConnection(String guardianEmail, Long starId) {
        UserEntity guardian = userRepository.findByEmail(guardianEmail)
                .orElseThrow(() -> new RuntimeException("Guardian not found"));

        UserConnectionEntity connection = new UserConnectionEntity(guardian.getId(), starId);
        userConnectionRepository.save(connection);
    }

    // 보호자의 모든 연결 조회
    public List<UserConnectionDto> getConnectionsByGuardian(Long guardianId) {
        return userConnectionRepository.findByGuardianId(guardianId).stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    // 특정 사용자의 보호자 조회
    public List<UserConnectionDto> getConnectionsByStar(Long starId) {
        return userConnectionRepository.findByStarId(starId).stream()
                .map(conn -> new UserConnectionDto(conn.getId(), conn.getGuardianId(), conn.getStarId(), conn.getConnectedAt()))
                .collect(Collectors.toList());
    }

    // 보호자 이메일로 조회
    public UserConnectionDto getConnectionsByGuardianEmail(String email) {
        UserEntity guardian = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Guardian not found"));
        UserConnectionEntity connection = userConnectionRepository.findByGuardianId(guardian.getId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No connections found for this guardian"));
        return new UserConnectionDto(connection.getId(), connection.getGuardianId(), connection.getStarId(), connection.getConnectedAt());
    }

    // 연결 삭제
    public void deleteConnection(Long id) {
        if (!userConnectionRepository.existsById(id)) {
            throw new RuntimeException("Connection not found");
        }
        userConnectionRepository.deleteById(id);
    }
}
