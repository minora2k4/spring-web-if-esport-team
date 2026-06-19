package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.BetRequest;
import web.dto.response.ApiResponse;
import web.dto.response.BetResponse;
import web.service.BetService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BetController extends BaseController {

    private final BetService betService;

    @GetMapping("/bets")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(betService.getAll(), "Chưa có kèo đấu nào!");
    }

    @GetMapping("/bets/pending")
    public ResponseEntity<ApiResponse<?>> getPending() {
        return listResponse(betService.getPending(), "Không có kèo nào đang đợi!");
    }

    @GetMapping("/bets/{id}")
    public ResponseEntity<ApiResponse<BetResponse>> getById(@PathVariable Long id) {
        return okResponse(betService.getById(id));
    }

    @PostMapping("/admin/bets")
    public ResponseEntity<ApiResponse<BetResponse>> create(@Valid @RequestBody BetRequest req) {
        return okResponse(betService.create(req));
    }

    @PutMapping("/admin/bets/{id}")
    public ResponseEntity<ApiResponse<BetResponse>> update(@PathVariable Long id,
                                                           @Valid @RequestBody BetRequest req) {
        return okResponse(betService.update(id, req));
    }

    @DeleteMapping("/admin/bets/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        betService.delete(id);
        return deletedResponse("kèo đấu");
    }
}