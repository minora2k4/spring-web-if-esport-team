package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import web.model.UploadedImage;
import web.repository.UploadedImageRepository;

import java.util.concurrent.TimeUnit;

/**
 * Phục vụ ảnh đã lưu trong database ở đường dẫn công khai /uploads/<filename>.
 * Thay thế cho cách phục vụ file tĩnh từ ổ đĩa trước đây (WebConfig).
 */
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final UploadedImageRepository imageRepository;

    // {filename:.+} để giữ luôn phần đuôi mở rộng (vd ".jpeg") trong biến filename.
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<byte[]> serve(@PathVariable String filename) {
        UploadedImage image = imageRepository.findById(filename)
                .orElseThrow(() -> new RuntimeException("Image not found: " + filename));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .body(image.getData());
    }
}
