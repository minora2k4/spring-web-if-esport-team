package web.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "offteam_photos")
@Data
public class OffteamPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private OffteamEvent event;
}
