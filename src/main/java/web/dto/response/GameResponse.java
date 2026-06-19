package web.dto.response;

import lombok.Data;
import web.model.Game;

@Data
public class GameResponse {
    private Long id;
    private String name;
    private String logoUrl;

    public static GameResponse from(Game game) {
        GameResponse res = new GameResponse();
        res.setId(game.getId());
        res.setName(game.getName());
        res.setLogoUrl(game.getLogoUrl());
        return res;
    }
}