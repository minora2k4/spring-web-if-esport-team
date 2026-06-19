package web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "team_logs")
@Data
public class TeamLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate eventDate;

    private String description;   // "Minora joined as ADC"

    private String type;          // "JOIN" / "LEAVE" / "ACHIEVEMENT"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}