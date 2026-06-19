package web.controller;

import org.springframework.http.ResponseEntity;
import web.dto.response.ApiResponse;
import java.util.List;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<?>> listResponse(
            List<T> data, String emptyMessage) {
        if (data.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.empty(emptyMessage));
        }
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> okResponse(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    protected ResponseEntity<ApiResponse<?>> deletedResponse(String entity) {
        return ResponseEntity.ok(ApiResponse.empty("Xóa " + entity + " thành công!"));
    }
}