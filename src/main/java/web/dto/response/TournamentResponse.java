package web.dto.response;

import lombok.Data;
import web.model.Tournament;
import java.time.LocalDate;

@Data
public class TournamentResponse {
    private Long id;
    private String name;
    private String achievement;
    private LocalDate startDate;
    private GameResponse game;

    public static TournamentResponse from(Tournament t) {
        TournamentResponse res = new TournamentResponse();
        res.setId(t.getId());
        res.setName(t.getName());
        res.setAchievement(t.getAchievement());
        res.setStartDate(t.getStartDate());
        if (t.getGame() != null) {
            res.setGame(GameResponse.from(t.getGame()));
        }
        return res;
    }
}