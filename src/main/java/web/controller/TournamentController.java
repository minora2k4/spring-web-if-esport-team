package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.TournamentRequest;
import web.dto.response.ApiResponse;
import web.dto.response.TournamentResponse;
import web.service.TournamentService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TournamentController extends BaseController {

    private final TournamentService tournamentService;

    @GetMapping("/tournaments")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(tournamentService.getAll(), "Chưa có giải đấu nào!");
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> getById(@PathVariable Long id) {
        return okResponse(tournamentService.getById(id));
    }

    @PostMapping("/admin/tournaments")
    public ResponseEntity<ApiResponse<TournamentResponse>> create(@Valid @RequestBody TournamentRequest req) {
        return okResponse(tournamentService.create(req));
    }

    @PutMapping("/admin/tournaments/{id}")
    public ResponseEntity<ApiResponse<TournamentResponse>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody TournamentRequest req) {
        return okResponse(tournamentService.update(id, req));
    }

    @DeleteMapping("/admin/tournaments/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return deletedResponse("giải đấu");
    }
}