package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class MemberRequest {

    @NotBlank(message = "In-game name is required")
    private String inGameName;

    private String realName;
    private String role;
    private String achievement;
    private String avatarUrl;
    private Boolean isActive = true;
    private List<Long> gameIds;   // ID các game thành viên chơi
}