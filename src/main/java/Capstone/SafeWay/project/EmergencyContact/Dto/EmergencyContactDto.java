package Capstone.SafeWay.project.EmergencyContact.Dto;

import Capstone.SafeWay.project.EmergencyContact.EmergencyContactEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmergencyContactDto {
    private String ename;
    private String ephone;

    public static EmergencyContactDto from(EmergencyContactEntity entity) {
        return EmergencyContactDto.builder()
                .ename(entity.getEname())
                .ephone(entity.getEphone())
                .build();
    }
}
