package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.TeamLogRequest;
import web.dto.response.ApiResponse;
import web.dto.response.TeamLogResponse;
import web.service.TeamLogService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamLogController extends BaseController {

    private final TeamLogService teamLogService;

    @GetMapping("/team-log")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(teamLogService.getAll(), "Chưa có log nào!");
    }

    @PostMapping("/admin/team-log")
    public ResponseEntity<ApiResponse<TeamLogResponse>> create(@Valid @RequestBody TeamLogRequest req) {
        return okResponse(teamLogService.create(req));
    }

    @PutMapping("/admin/team-log/{id}")
    public ResponseEntity<ApiResponse<TeamLogResponse>> update(@PathVariable Long id,
                                                               @Valid @RequestBody TeamLogRequest req) {
        return okResponse(teamLogService.update(id, req));
    }

    @DeleteMapping("/admin/team-log/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        teamLogService.delete(id);
        return deletedResponse("log");
    }
}