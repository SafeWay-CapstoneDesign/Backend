package Capstone.SafeWay.project.Location;

import Capstone.SafeWay.project.Location.Dto.LocationRequestDto;
import Capstone.SafeWay.project.Location.Dto.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public String create(LocationRequestDto dto) {
        log.info("받은 DTO: start={}, end={}, distance={}, time={}, lat={}, lon={}",
                dto.getStartName(), dto.getEndName(), dto.getTDistance(), dto.getTTime(),
                dto.getLatitude(), dto.getLongitude());

        LocationEntity entity = LocationEntity.builder()
                .startName(dto.getStartName())
                .endName(dto.getEndName())
                .tDistance(dto.getTDistance())  // 여기 로그 찍어봐야 함
                .tTime(dto.getTTime())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        log.info("엔티티로 변환된 값: {}", entity);

        locationRepository.save(entity);
        return "저장 완료";
    }


    public List<LocationResponseDto> getAll() {
        return locationRepository.findAll().stream()
                .map(LocationResponseDto::from)
                .collect(Collectors.toList());
    }
}
