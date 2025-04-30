package Capstone.SafeWay.project.Location;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "location")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String startName;

    @Column(nullable = false)
    private String endName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tDistance;

    @Column(nullable = false)
    private Time tTime;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Long userId;

    public void updateLocation(String startName, String endName, BigDecimal tDistance, Time tTime, Double latitude, Double longitude) {
        this.startName = startName;
        this.endName = endName;
        this.tDistance = tDistance;
        this.tTime = tTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
