package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TournamentRequest {

    @NotBlank(message = "Tournament name is required")
    private String name;

    private String achievement;
    private LocalDate startDate;

    @NotNull(message = "Game is required")
    private Long gameId;
}