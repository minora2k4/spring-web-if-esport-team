package web.dto.response;

import lombok.Data;
import web.model.Member;
import java.util.List;

@Data
public class MemberResponse {
    private Long id;
    private String inGameName;
    private String realName;
    private String role;
    private String achievement;
    private String avatarUrl;
    private Boolean isActive;
    private List<GameResponse> games;

    public static MemberResponse from(Member member) {
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setInGameName(member.getInGameName());
        res.setRealName(member.getRealName());
        res.setRole(member.getRole());
        res.setAchievement(member.getAchievement());
        res.setAvatarUrl(member.getAvatarUrl());
        res.setIsActive(member.getIsActive());
        if (member.getGames() != null) {
            res.setGames(member.getGames().stream()
                    .map(GameResponse::from)
                    .toList());
        }
        return res;
    }
}