package Capstone.SafeWay.project.EmergencyContact;

import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactDto;
import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactRequestDto;
import Capstone.SafeWay.project.User.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;

    public String create(EmergencyContactRequestDto dto, UserEntity user) {
        EmergencyContactEntity contact = EmergencyContactEntity.builder()
                .ename(dto.getEname())
                .ephone(dto.getEphone())
                .user(user)
                .build();
        emergencyContactRepository.save(contact);
        return "비상 연락망 등록 완료";
    }

    public List<EmergencyContactDto> getMyContacts(UserEntity user) {
        return emergencyContactRepository.findByUser(user).stream()
                .map(EmergencyContactDto::from)
                .collect(Collectors.toList());
    }

    public String delete(Long id, UserEntity user) {
        EmergencyContactEntity contact = emergencyContactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("연락망을 찾을 수 없습니다."));

        if (!contact.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("본인의 연락망만 삭제할 수 있습니다.");
        }

        emergencyContactRepository.delete(contact);
        return "비상 연락망 삭제 완료";
    }
}