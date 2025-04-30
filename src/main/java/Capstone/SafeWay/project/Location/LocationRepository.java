package Capstone.SafeWay.project.Location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    LocationEntity findByUserId(Long userId);
}
