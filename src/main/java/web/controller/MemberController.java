package web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.dto.request.MemberRequest;
import web.dto.response.ApiResponse;
import web.dto.response.MemberResponse;
import web.service.MemberService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController extends BaseController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<ApiResponse<?>> getActive() {
        return listResponse(memberService.getActive(), "Chưa có thành viên nào!");
    }

    @GetMapping("/members/all")
    public ResponseEntity<ApiResponse<?>> getAll() {
        return listResponse(memberService.getAll(), "Chưa có thành viên nào!");
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getById(@PathVariable Long id) {
        return okResponse(memberService.getById(id));
    }

    @PostMapping("/admin/members")
    public ResponseEntity<ApiResponse<MemberResponse>> create(@Valid @RequestBody MemberRequest req) {
        return okResponse(memberService.create(req));
    }

    @PutMapping("/admin/members/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> update(@PathVariable Long id,
                                                              @Valid @RequestBody MemberRequest req) {
        return okResponse(memberService.update(id, req));
    }

    @DeleteMapping("/admin/members/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        memberService.delete(id);
        return deletedResponse("thành viên");
    }
}