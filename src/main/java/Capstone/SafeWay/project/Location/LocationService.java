package Capstone.SafeWay.project.Location;

import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.Location.Dto.LocationRequestDto;
import Capstone.SafeWay.project.Location.Dto.LocationResponseDto;
import Capstone.SafeWay.project.UserConnections.UserConnectionEntity;
import Capstone.SafeWay.project.UserConnections.UserConnectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserConnectionRepository userConnectionRepository;

    @CacheEvict(value = "locationCache", key = "#userId")
    @Transactional
    public String createOrUpdateLocation(Long userId, LocationRequestDto dto) {
        LocationEntity existingLocation = locationRepository.findByUserId(userId);

        if (existingLocation != null) {
            existingLocation.updateLocation(dto.getStartName(), dto.getEndName(), dto.getTDistance(), dto.getTTime(), dto.getLatitude(), dto.getLongitude());
            locationRepository.save(existingLocation);
            return "위치 정보가 업데이트 되었습니다.";
        }

        LocationEntity newLocation = LocationEntity.builder()
                .userId(userId)
                .startName(dto.getStartName())
                .endName(dto.getEndName())
                .tDistance(dto.getTDistance())
                .tTime(dto.getTTime())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        locationRepository.save(newLocation);
        return "새로운 위치 정보가 등록되었습니다.";
    }

    public LocationResponseDto getLocationsByUser(Long userId) {
        UserConnectionEntity connection = userConnectionRepository.findFirstByGuardianIdOrStarId(userId, userId);

        if (connection == null) {
            log.warn("현재 사용자와 연결된 다른 사용자가 없습니다.");
            throw new CustomException(ErrorCode.NO_CONNECTION_FOUND);
        }

        Long connectedUserId = (connection.getGuardianId() == userId) ? connection.getStarId() : connection.getGuardianId();

        LocationEntity location = locationRepository.findByUserId(connectedUserId);

        if (location == null) {
            log.warn("로그인된 사용자의 위치 정보가 존재하지 않습니다.");
            throw new CustomException(ErrorCode.NO_LOCATION_FOUND);
        }

        return LocationResponseDto.from(location);
    }
}
