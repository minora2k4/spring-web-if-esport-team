package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import web.dto.request.GameRequest;
import web.dto.response.GameResponse;
import web.model.Game;
import web.repository.GameRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService extends BaseService<Game, Long> {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;

    @Override
    protected JpaRepository<Game, Long> getRepository() { return gameRepository; }

    @Override
    protected String getEntityName() { return "Game"; }

    public List<GameResponse> getAll() {
        return gameRepository.findAll().stream().map(GameResponse::from).toList();
    }

    public GameResponse getById(Long id) {
        return GameResponse.from(findOrThrow(id));
    }

    public GameResponse create(GameRequest request) {
        if (gameRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Game already exists: " + request.getName());
        }
        Game game = new Game();
        game.setName(request.getName());
        game.setLogoUrl(request.getLogoUrl());
        Game saved = gameRepository.save(game);
        log.info("[GAME] Created: {}", saved.getName());
        return GameResponse.from(saved);
    }

    public GameResponse update(Long id, GameRequest request) {
        Game game = findOrThrow(id);
        game.setName(request.getName());
        game.setLogoUrl(request.getLogoUrl());
        log.info("[GAME] Updated: {}", game.getName());
        return GameResponse.from(gameRepository.save(game));
    }

    public void delete(Long id) {
        findOrThrow(id);
        gameRepository.deleteById(id);
        log.info("[GAME] Deleted id: {}", id);
    }
}