package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.UploadedImage;

public interface UploadedImageRepository extends JpaRepository<UploadedImage, String> {
}
