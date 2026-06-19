package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OffteamEventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private LocalDate eventDate;
    private String coverPhotoUrl;
    private List<String> photoUrls;  // danh sách link ảnh
}