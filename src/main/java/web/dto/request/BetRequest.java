package web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BetRequest {

    @NotBlank(message = "Opponent is required")
    private String opponent;

    private String result;   // "WIN" / "LOSE" / "PENDING"
    private Double amount;
    private LocalDate betDate;

    @NotNull(message = "Game is required")
    private Long gameId;
}