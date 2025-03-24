package Capstone.SafeWay.project.Cane;

import Capstone.SafeWay.project.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaneRepository extends JpaRepository<CaneEntity, Long> {
    Optional<CaneEntity> findByUser(UserEntity user);
    boolean existsByBluetoothId(String bluetoothId);
}