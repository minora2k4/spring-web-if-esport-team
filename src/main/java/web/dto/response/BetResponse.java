package web.dto.response;

import lombok.Data;
import web.model.Bet;
import java.time.LocalDate;

@Data
public class BetResponse {
    private Long id;
    private String opponent;
    private String result;
    private Double amount;
    private LocalDate betDate;
    private GameResponse game;

    public static BetResponse from(Bet b) {
        BetResponse res = new BetResponse();
        res.setId(b.getId());
        res.setOpponent(b.getOpponent());
        res.setResult(b.getResult());
        res.setAmount(b.getAmount());
        res.setBetDate(b.getBetDate());
        if (b.getGame() != null) {
            res.setGame(GameResponse.from(b.getGame()));
        }
        return res;
    }
}