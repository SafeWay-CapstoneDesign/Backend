package Capstone.SafeWay.project.Location;

import Capstone.SafeWay.project.Location.Dto.LocationRequestDto;
import Capstone.SafeWay.project.Location.Dto.LocationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "위치 등록",
            description = "위치 정보를 등록하는 API입니다. 출발지, 도착지, 거리, 시간, 위도 및 경도를 입력하여 위치를 등록합니다. 시간은 HH:mm:ss 형태로 보내주세요.",
            tags = {"Location"}
    )
    @PostMapping
    public ResponseEntity<String> create(@RequestBody LocationRequestDto dto) {
        log.info("위치 등록 요청: startName={}, endName={}, distance={}, time={}, lat={}, lon={}",
                dto.getStartName(), dto.getEndName(), dto.getTDistance(), dto.getTTime(),
                dto.getLatitude(), dto.getLongitude());

        String result = locationService.create(dto);
        log.info("위치 등록 완료");

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "위치 전체 조회",
            description = "등록된 모든 위치 정보를 조회하는 API입니다.",
            tags = {"Location"}
    )
    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAll() {
        log.info("위치 전체 조회 요청");
        List<LocationResponseDto> locations = locationService.getAll();
        log.info("조회된 위치 개수: {}", locations.size());

        return ResponseEntity.ok(locations);
    }
}
