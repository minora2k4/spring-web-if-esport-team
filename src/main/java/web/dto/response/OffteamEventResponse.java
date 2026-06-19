package web.dto.response;

import lombok.Data;
import web.model.OffteamEvent;
import java.time.LocalDate;
import java.util.List;

@Data
public class OffteamEventResponse {
    private Long id;
    private String title;
    private LocalDate eventDate;
    private String coverPhotoUrl;
    private List<String> photoUrls;

    public static OffteamEventResponse from(OffteamEvent e) {
        OffteamEventResponse res = new OffteamEventResponse();
        res.setId(e.getId());
        res.setTitle(e.getTitle());
        res.setEventDate(e.getEventDate());
        res.setCoverPhotoUrl(e.getCoverPhotoUrl());
        if (e.getPhotos() != null) {
            res.setPhotoUrls(e.getPhotos().stream()
                    .map(p -> p.getPhotoUrl())
                    .toList());
        }
        return res;
    }
}