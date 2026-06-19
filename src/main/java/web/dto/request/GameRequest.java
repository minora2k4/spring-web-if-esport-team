package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GameRequest {

    @NotBlank(message = "Game name is required")
    private String name;

    private String logoUrl;
}