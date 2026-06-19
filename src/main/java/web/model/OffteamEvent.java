package web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "offteam_events")
@Data
public class OffteamEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;           // "Off Team tháng 3/2025"

    private LocalDate eventDate;

    private String coverPhotoUrl;   // Ảnh bìa của buổi off

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OffteamPhoto> photos;
}