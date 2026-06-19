package web.dto.response;

import lombok.Data;
import web.model.TeamLog;
import java.time.LocalDate;

@Data
public class TeamLogResponse {
    private Long id;
    private String description;
    private String type;
    private LocalDate eventDate;
    private MemberResponse member;

    public static TeamLogResponse from(TeamLog t) {
        TeamLogResponse res = new TeamLogResponse();
        res.setId(t.getId());
        res.setDescription(t.getDescription());
        res.setType(t.getType());
        res.setEventDate(t.getEventDate());
        if (t.getMember() != null) {
            res.setMember(MemberResponse.from(t.getMember()));
        }
        return res;
    }
}