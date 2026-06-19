package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.GameRequest;
import web.dto.response.ApiResponse;
import web.dto.response.GameResponse;
import web.service.GameService;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameController extends BaseController {

    private final GameService gameService;

    @GetMapping("/games")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(gameService.getAll(), "Chưa có game nào!");
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<ApiResponse<GameResponse>> getById(@PathVariable Long id) {
        return okResponse(gameService.getById(id));
    }

    @PostMapping("/admin/games")
    public ResponseEntity<ApiResponse<GameResponse>> create(@Valid @RequestBody GameRequest req) {
        return okResponse(gameService.create(req));
    }

    @PutMapping("/admin/games/{id}")
    public ResponseEntity<ApiResponse<GameResponse>> update(@PathVariable Long id,
                                                            @Valid @RequestBody GameRequest req) {
        return okResponse(gameService.update(id, req));
    }

    @DeleteMapping("/admin/games/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        gameService.delete(id);
        return deletedResponse("game");
    }
}