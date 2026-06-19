package web.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.request.BetRequest;
import web.dto.response.BetResponse;
import web.model.Bet;
import web.model.Game;
import web.repository.BetRepository;
import web.repository.GameRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BetService extends BaseService<Bet, Long> {

    private static final Logger log = LoggerFactory.getLogger(BetService.class);
    private final BetRepository betRepository;
    private final GameRepository gameRepository;

    @Override
    protected JpaRepository<Bet, Long> getRepository() { return betRepository; }

    @Override
    protected String getEntityName() { return "Bet"; }

    public List<BetResponse> getAll() {
        return betRepository.findAllWithGame().stream().map(BetResponse::from).toList();
    }

    public List<BetResponse> getPending() {
        return betRepository.findAllWithGame().stream()
                .filter(b -> "PENDING".equals(b.getResult()))
                .map(BetResponse::from).toList();
    }

    public BetResponse getById(Long id) {
        return BetResponse.from(findOrThrow(id));
    }

    public BetResponse create(BetRequest request) {
        Bet bet = buildBet(new Bet(), request);
        Bet saved = betRepository.save(bet);
        log.info("[BET] Created vs {}", saved.getOpponent());
        return BetResponse.from(saved);
    }

    public BetResponse update(Long id, BetRequest request) {
        Bet bet = findOrThrow(id);
        buildBet(bet, request);
        log.info("[BET] Updated id: {}", id);
        return BetResponse.from(betRepository.save(bet));
    }

    public void delete(Long id) {
        findOrThrow(id);
        betRepository.deleteById(id);
        log.info("[BET] Deleted id: {}", id);
    }

    private Bet buildBet(Bet bet, BetRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found: " + request.getGameId()));
        bet.setOpponent(request.getOpponent());
        bet.setResult(request.getResult() != null ? request.getResult() : "PENDING");
        bet.setAmount(request.getAmount());
        bet.setBetDate(request.getBetDate());
        bet.setGame(game);
        return bet;
    }
}