package web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ảnh upload được lưu trực tiếp trong database (cột bytea) thay vì ghi ra ổ đĩa.
 * Lý do: trên môi trường không có disk persistent (vd Render free tier), file trên
 * ổ đĩa sẽ mất sau mỗi lần restart; còn dữ liệu DB thì luôn được giữ nguyên.
 */
@Entity
@Table(name = "uploaded_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedImage {

    /** Tên file dạng "<uuid>.<ext>", đồng thời là phần cuối của URL /uploads/<filename>. */
    @Id
    private String filename;

    @Column(nullable = false)
    private String contentType;

    /** Nội dung nhị phân của ảnh; map sang cột bytea của PostgreSQL. */
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] data;
}
