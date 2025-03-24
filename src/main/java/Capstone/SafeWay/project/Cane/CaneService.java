package Capstone.SafeWay.project.Cane;

import Capstone.SafeWay.project.Cane.Dto.CaneRequestDto;
import Capstone.SafeWay.project.Cane.Dto.CaneResponseDto;
import Capstone.SafeWay.project.User.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaneService {

    private final CaneRepository caneRepository;

    public String registerCane(CaneRequestDto dto, UserEntity user) {
        if (caneRepository.existsByBluetoothId(dto.getBluetoothId())) {
            throw new IllegalArgumentException("이미 등록된 블루투스 ID입니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("지팡이를 찾을 수 없습니다"));
        cane.connectToUser(user);
        caneRepository.save(cane);
        return "지팡이 연결 완료";
    }

    public String disconnectCane(Long caneId, UserEntity user) {
        CaneEntity cane = caneRepository.findById(caneId)
                .orElseThrow(() -> new IllegalArgumentException("지팡이를 찾을 수 없습니다"));

        if (!cane.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("본인의 지팡이만 삭제할 수 있습니다");
        }

        caneRepository.delete(cane);
        return "지팡이 삭제 완료";
    }

}
