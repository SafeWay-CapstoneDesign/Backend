package Capstone.SafeWay.project.Location.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequestDto {
    private String startName;
    private String endName;

    @JsonProperty("tdistance")
    private BigDecimal tDistance;

    @JsonProperty("ttime")
    private Time tTime;

    private Double latitude;
    private Double longitude;
}
