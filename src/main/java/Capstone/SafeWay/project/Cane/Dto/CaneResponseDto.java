package Capstone.SafeWay.project.Cane.Dto;

import Capstone.SafeWay.project.Cane.CaneEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CaneResponseDto {
    private String name;

    public static CaneResponseDto from(CaneEntity cane) {
        return CaneResponseDto.builder()
                .name(cane.getName())
                .build();
    }
}
