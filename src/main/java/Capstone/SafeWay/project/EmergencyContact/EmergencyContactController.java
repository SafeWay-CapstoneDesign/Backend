package Capstone.SafeWay.project.EmergencyContact;

import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactDto;
import Capstone.SafeWay.project.EmergencyContact.Dto.EmergencyContactRequestDto;
import Capstone.SafeWay.project.Global.Security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "비상 연락망 등록",
            description = "사용자의 비상 연락망을 등록하는 API입니다.",
            tags = {"EmergencyContact"}
    )
    @PostMapping
    public ResponseEntity<String> create(@RequestBody EmergencyContactRequestDto dto) {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("비상 연락망 등록 요청: userId={}, name={}", currentUser.getUser().getId(), dto.getEname());
        return ResponseEntity.ok(emergencyContactService.create(dto, currentUser.getUser()));
    }

    @Operation(
            summary = "비상 연락망 조회",
            description = "사용자가 등록한 비상 연락망을 조회하는 API입니다.",
            tags = {"EmergencyContact"}
    )
    @GetMapping
    public ResponseEntity<List<EmergencyContactDto>> getMyContacts() {
        UserDetailsImpl currentUser = getCurrentUser();
        log.info("비상 연락망 조회 요청: userId={}", currentUser.getUser().getId());
        return ResponseEntity.ok(emergencyContactService.getMyContacts(currentUser.getUser()));
    }

    @Operation(
            summary = "비상 연락망 삭제",
            description = "사용자가 등록한 비상 연락망을 삭제하는 API입니다.",
            tags = {"EmergencyContact"}
    )
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
