package Capstone.SafeWay.project.Global.Exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_USER(HttpStatus.UNAUTHORIZED, "잘못된 사용자입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    CONNECTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 사용자와 보호자가 연결되어 있습니다."),
    CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "연결된 관계가 존재하지 않습니다."),
    GUARDIAN_NOT_FOUND(HttpStatus.NOT_FOUND, "보호자를 찾을 수 없습니다."),
    NO_CONNECTION_FOR_GUARDIAN(HttpStatus.NOT_FOUND, "해당 보호자에 대한 연결이 존재하지 않습니다."),
    CANE_NOT_FOUND(HttpStatus.NOT_FOUND, "지팡이를 찾을 수 없습니다."),
    DUPLICATE_ENTRY(HttpStatus.CONFLICT, "이미 등록된 블루투스 ID입니다."),
    INVALID_CANE_OWNER(HttpStatus.FORBIDDEN, "본인의 지팡이만 삭제할 수 있습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
