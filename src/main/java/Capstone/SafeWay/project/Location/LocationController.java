package Capstone.SafeWay.project.Location;

import Capstone.SafeWay.project.Location.Dto.LocationRequestDto;
import Capstone.SafeWay.project.Location.Dto.LocationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody LocationRequestDto dto) {
        log.info("위치 등록 요청: startName={}, endName={}, distance={}, time={}, lat={}, lon={}",
                dto.getStartName(), dto.getEndName(), dto.getTDistance(), dto.getTTime(),
                dto.getLatitude(), dto.getLongitude());

        String result = locationService.create(dto);
        log.info("위치 등록 완료");

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAll() {
        log.info("위치 전체 조회 요청");
        List<LocationResponseDto> locations = locationService.getAll();
        log.info("조회된 위치 개수: {}", locations.size());

        return ResponseEntity.ok(locations);
    }
}
