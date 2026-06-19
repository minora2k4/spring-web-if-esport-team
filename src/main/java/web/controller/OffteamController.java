package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.OffteamEventRequest;
import web.dto.response.ApiResponse;
import web.dto.response.OffteamEventResponse;
import web.service.OffteamService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OffteamController extends BaseController {

    private final OffteamService offteamService;

    @GetMapping("/offteam")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(offteamService.getAll(), "Chưa có sự kiện off team nào!");
    }

    @GetMapping("/offteam/{id}")
    public ResponseEntity<ApiResponse<OffteamEventResponse>> getById(@PathVariable Long id) {
        return okResponse(offteamService.getById(id));
    }

    @PostMapping("/admin/offteam")
    public ResponseEntity<ApiResponse<OffteamEventResponse>> create(@Valid @RequestBody OffteamEventRequest req) {
        return okResponse(offteamService.create(req));
    }

    @PutMapping("/admin/offteam/{id}")
    public ResponseEntity<ApiResponse<OffteamEventResponse>> update(@PathVariable Long id,
                                                                    @Valid @RequestBody OffteamEventRequest req) {
        return okResponse(offteamService.update(id, req));
    }

    @DeleteMapping("/admin/offteam/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        offteamService.delete(id);
        return deletedResponse("sự kiện off team");
    }
}