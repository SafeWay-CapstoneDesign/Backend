package Capstone.SafeWay.project.Location.Dto;

import Capstone.SafeWay.project.Location.LocationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@AllArgsConstructor
public class LocationResponseDto {
    private Long id;
    private String startName;
    private String endName;
    private BigDecimal tDistance;
    private Time tTime;
    private Double latitude;
    private Double longitude;

    public static LocationResponseDto from(LocationEntity entity) {
        return new LocationResponseDto(
                entity.getId(),
                entity.getStartName(),
                entity.getEndName(),
                entity.getTDistance(),
                entity.getTTime(),
                entity.getLatitude(),
                entity.getLongitude()
        );
    }
}
