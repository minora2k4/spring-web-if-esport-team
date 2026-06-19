package web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // AUTH
    // Sai username hoặc password
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("[AUTH] Bad credentials attempt");
        return response(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu!");
    }

    // User không tồn tại
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UsernameNotFoundException ex) {
        log.warn("[AUTH] User not found: {}", ex.getMessage());
        return response(HttpStatus.UNAUTHORIZED, "Tài khoản không tồn tại!");
    }

    // Tài khoản bị disable
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(DisabledException ex) {
        log.warn("[AUTH] Account disabled");
        return response(HttpStatus.UNAUTHORIZED, "Tài khoản đã bị vô hiệu hóa!");
    }

    // Không có quyền truy cập (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("[AUTH] Access denied: {}", ex.getMessage());
        return response(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này!");
    }

    // VALIDATION
    // Lỗi @NotBlank, @NotNull, @Valid,...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
        log.warn("[VALIDATION] Errors: {}", fieldErrors);

        Map<String, Object> body = new HashMap<>();
        body.put("error", "Dữ liệu không hợp lệ!");
        body.put("details", fieldErrors);
        return ResponseEntity.badRequest().body(body);
    }

    // Sai kiểu dữ liệu trên URL (vd: /members/abc thay vì /members/1)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        log.warn("[PARAM] Type mismatch: {}", ex.getMessage());
        return response(HttpStatus.BAD_REQUEST,
                "Tham số không hợp lệ: '" + ex.getName() + "' phải là số nguyên!");
    }

    // BUSINESS
    // Không tìm thấy dữ liệu (not found)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage();
        log.error("[ERROR] {}", message);

        // Phân loại thông báo theo nội dung
        if (message != null) {
            if (message.contains("not found") || message.contains("Not found")) {
                return response(HttpStatus.NOT_FOUND, mapNotFoundMessage(message));
            }
            if (message.contains("already exists")) {
                return response(HttpStatus.CONFLICT, mapConflictMessage(message));
            }
        }

        return response(HttpStatus.BAD_REQUEST, message != null ? message : "Có lỗi xảy ra!");
    }

    // HELPERS ====
    // Map "not found" message sang tiếng Việt
    private String mapNotFoundMessage(String message) {
        if (message.contains("Game"))       return "Không tìm thấy game!";
        if (message.contains("Member"))     return "Không tìm thấy thành viên!";
        if (message.contains("Tournament")) return "Không tìm thấy giải đấu!";
        if (message.contains("Bet"))        return "Không tìm thấy kèo đấu!";
        if (message.contains("TeamLog"))    return "Không tìm thấy log!";
        if (message.contains("Offteam"))    return "Không tìm thấy sự kiện off team!";
        return "Không tìm thấy dữ liệu!";
    }

    // Map "already exists" message sang tiếng Việt
    private String mapConflictMessage(String message) {
        if (message.contains("Game")) return "Game này đã tồn tại!";
        return "Dữ liệu đã tồn tại!";
    }

    // Build response body chuẩn
    private ResponseEntity<Map<String, String>> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}