package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TeamLogRequest {

    @NotBlank(message = "Description is required")
    private String description;

    private String type;        // "JOIN" / "LEAVE" / "ACHIEVEMENT"
    private LocalDate eventDate;
    private Long memberId;      // nullable — log có thể không gắn member cụ thể
}