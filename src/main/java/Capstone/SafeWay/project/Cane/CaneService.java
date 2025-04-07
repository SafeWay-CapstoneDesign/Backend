package Capstone.SafeWay.project.Cane;

import Capstone.SafeWay.project.Cane.Dto.CaneRequestDto;
import Capstone.SafeWay.project.Cane.Dto.CaneResponseDto;
import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.User.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaneService {

    private final CaneRepository caneRepository;

    public String registerCane(CaneRequestDto dto, UserEntity user) {
        if (caneRepository.existsByBluetoothId(dto.getBluetoothId())) {
            throw new CustomException(ErrorCode.DUPLICATE_ENTRY);
        }

        CaneEntity cane = CaneEntity.builder()
                .name(dto.getName())
                .bluetoothId(dto.getBluetoothId())
                .build();

        cane.connectToUser(user);
        caneRepository.save(cane);

        return "지팡이 등록 완료";
    }

    public List<CaneResponseDto> getMyCanes(UserEntity user) {
        return caneRepository.findByUser(user)
                .stream()
                .map(CaneResponseDto::from)
                .collect(Collectors.toList());
    }

    public String connectCane(Long caneId, UserEntity user) {
        CaneEntity cane = caneRepository.findById(caneId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANE_NOT_FOUND));
        cane.connectToUser(user);
        caneRepository.save(cane);
        return "지팡이 연결 완료";
    }

    public String disconnectCane(Long caneId, UserEntity user) {
        CaneEntity cane = caneRepository.findById(caneId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANE_NOT_FOUND));

        if (!cane.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.INVALID_CANE_OWNER);
        }

        caneRepository.delete(cane);
        return "지팡이 삭제 완료";
    }

}
