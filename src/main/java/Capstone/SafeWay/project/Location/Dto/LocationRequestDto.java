package Capstone.SafeWay.project.Location.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal tDistance;

    @JsonFormat(pattern = "HH:mm:ss")
    private Time tTime;

    private Double latitude;
    private Double longitude;
}
