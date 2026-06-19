package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.request.TournamentRequest;
import web.dto.response.TournamentResponse;
import web.model.Game;
import web.model.Tournament;
import web.repository.GameRepository;
import web.repository.TournamentRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentService extends BaseService<Tournament, Long> {

    private static final Logger log = LoggerFactory.getLogger(TournamentService.class);
    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;

    @Override
    protected JpaRepository<Tournament, Long> getRepository() { return tournamentRepository; }

    @Override
    protected String getEntityName() { return "Tournament"; }

    public List<TournamentResponse> getAll() {
        return tournamentRepository.findAllWithGame().stream()
                .map(TournamentResponse::from).toList();
    }

    public TournamentResponse getById(Long id) {
        return TournamentResponse.from(findOrThrow(id));
    }

    public TournamentResponse create(TournamentRequest request) {
        Tournament t = buildTournament(new Tournament(), request);
        Tournament saved = tournamentRepository.save(t);
        log.info("[TOURNAMENT] Created: {}", saved.getName());
        return TournamentResponse.from(saved);
    }

    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament t = findOrThrow(id);
        buildTournament(t, request);
        log.info("[TOURNAMENT] Updated: {}", t.getName());
        return TournamentResponse.from(tournamentRepository.save(t));
    }

    public void delete(Long id) {
        findOrThrow(id);
        tournamentRepository.deleteById(id);
        log.info("[TOURNAMENT] Deleted id: {}", id);
    }

    private Tournament buildTournament(Tournament t, TournamentRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found: " + request.getGameId()));
        t.setName(request.getName());
        t.setAchievement(request.getAchievement());
        t.setStartDate(request.getStartDate());
        t.setGame(game);
        return t;
    }
}