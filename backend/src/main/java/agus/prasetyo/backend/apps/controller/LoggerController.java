package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.apps.model.dto.ArticleDTO;
import agus.prasetyo.backend.apps.model.dto.LoggerDTO;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.service.LoggerService;
import agus.prasetyo.backend.apps.service.UserService;
import agus.prasetyo.backend.system.utils.ApiResponse;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
public class LoggerController {

    private final LoggerService loggerService;

    public LoggerController(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ALL','LOG_READ')")
    public ResponseEntity<ApiResponse<List<LoggerDTO>>> index(@RequestParam(required = false, defaultValue = "created_at") String sortBy,
                                                              @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<LoggerDTO> lists = loggerService.findAll(sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", lists));
    }
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ALL','LOG_DETAIL')")
    public ResponseEntity<ApiResponse<LoggerDTO>> detail(@PathVariable UUID id) throws ResourceNotFoundException {
        LoggerDTO el = loggerService.findByIdOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", el));
    }

    @DeleteMapping("/delete_all")
    @PreAuthorize("hasAnyRole('ALL','LOG_DELETE_ALL')")
    public ResponseEntity<ApiResponse<String>> deleteAll() {
        loggerService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", null));
    }
}
