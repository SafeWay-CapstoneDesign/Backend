package Capstone.SafeWay.project.EmergencyContact;

import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactDto;
import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactRequestDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emergency")
@RequiredArgsConstructor
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody EmergencyContactRequestDto dto) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("비상 연락망 등록 요청: userId={}, name={}", currentUser.getUser().getId(), dto.getEname());
        return ResponseEntity.ok(emergencyContactService.create(dto, currentUser.getUser()));
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContactDto>> getMyContacts() {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("비상 연락망 조회 요청: userId={}", currentUser.getUser().getId());
        return ResponseEntity.ok(emergencyContactService.getMyContacts(currentUser.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("비상 연락망 삭제 요청: userId={}, contactId={}", currentUser.getUser().getId(), id);
        return ResponseEntity.ok(emergencyContactService.delete(id, currentUser.getUser()));
    }

    private UserDetailsImpl getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) auth.getPrincipal();
    }
}
