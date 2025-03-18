package Capstone.SafeWay.project.UserConnections;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, Long> {
    List<UserConnectionEntity> findByGuardianId(Long guardianId);
    List<UserConnectionEntity> findByStarId(Long starId);
}
