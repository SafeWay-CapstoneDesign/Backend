package Capstone.SafeWay.project.Notice.Dto;

import lombok.Data;

@Data
public class FcmSendRequestDto {
    private Long userId;
    private String category;
    private String content;
}

