package Capstone.SafeWay.project.Location;

import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import Capstone.SafeWay.project.Location.Dto.LocationRequestDto;
import Capstone.SafeWay.project.Location.Dto.LocationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PostMapping()
    public ResponseEntity<String> createOrUpdateLocation(
            @RequestBody LocationRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        Long userId = currentUser.getUser().getId();  // 현재 로그인된 사용자 ID
        String result = locationService.createOrUpdateLocation(userId, dto);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "위치 조회 (로그인된 사용자의 연결된 위치 조회)",
            description = "로그인된 사용자의 연결된 다른 사용자의 위치 정보를 조회하는 API입니다.",
            tags = {"Location"}
    )
    @GetMapping("/connected")
    public ResponseEntity<LocationResponseDto> getLocationsByUser(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        Long userId = currentUser.getUser().getId();  // 현재 로그인된 사용자의 ID를 가져옴
        log.info("현재 로그인된 사용자 ID: {}", userId);

        // 연결된 사용자들의 위치 정보 조회
        LocationResponseDto location = locationService.getLocationsByUser(userId);

        if (location == null) {
            log.warn("로그인된 사용자의 연결된 위치 정보가 없습니다.");
            return ResponseEntity.status(404).build();  // 연결된 위치 정보가 없으면 404 반환
        }

        return ResponseEntity.ok(location);
    }
}
