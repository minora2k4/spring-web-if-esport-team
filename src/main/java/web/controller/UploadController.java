package web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.dto.response.ApiResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Nhận file ảnh tải lên từ thiết bị (máy tính / điện thoại), lưu vào ổ đĩa
 * và trả về URL công khai để client dùng cho avatar / logo / ảnh offteam.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UploadController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn một file ảnh!");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("File tải lên phải là ảnh!");
        }

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String ext = extractExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + ext;
            Path target = dir.resolve(filename);
            file.transferTo(target);

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(filename)
                    .toUriString();

            log.info("[UPLOAD] Saved {} ({} bytes)", filename, file.getSize());
            return okResponse(url);
        } catch (IOException e) {
            log.error("[UPLOAD] Failed: {}", e.getMessage());
            throw new RuntimeException("Tải ảnh lên thất bại!");
        }
    }

    private String extractExtension(String original) {
        if (original == null) return "";
        int dot = original.lastIndexOf('.');
        if (dot < 0) return "";
        String ext = original.substring(dot).toLowerCase();
        // chỉ giữ phần mở rộng an toàn, ngắn gọn
        return ext.matches("\\.[a-z0-9]{1,5}") ? ext : "";
    }
}
