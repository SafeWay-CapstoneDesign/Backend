package Capstone.SafeWay.project.EmergencyContact;

import Capstone.SafeWay.project.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContactEntity, Long> {
    List<EmergencyContactEntity> findByUser(UserEntity user);
}