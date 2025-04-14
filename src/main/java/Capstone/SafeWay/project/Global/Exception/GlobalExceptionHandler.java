package Capstone.SafeWay.project.Global.Exception;

import Capstone.SafeWay.project.Global.Exception.error.CustomException;
import Capstone.SafeWay.project.Global.Exception.error.ErrorCode;
import Capstone.SafeWay.project.Global.Exception.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getStatus().value(), errorCode.getMessage(), errorCode.name()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntry(DataIntegrityViolationException ex) {

        log.error(ex.getMessage());
        log.error(ex.getStackTrace().toString());
        return ResponseEntity.status(409)
                .body(new ErrorResponse(409, "중복된 값이 존재합니다.", "DUPLICATE_ENTRY"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(403)
                .body(new ErrorResponse(403, ex.getMessage(), "ACCESS_DENIED"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ResponseEntity.status(500)
                .body(new ErrorResponse(500, ex.getMessage(), "INTERNAL_SERVER_ERROR"));
    }
}
