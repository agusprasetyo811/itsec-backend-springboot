package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.apps.model.dto.RoleDTO;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.model.request.RegisterRequest;
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
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ALL','USER_READ')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> index(@RequestParam(required = false, defaultValue = "created_at") String sortBy,
                                                            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<UserDTO> userLists = userService.findAll(sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", userLists));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ALL','USER_DETAIL')")
    public ResponseEntity<ApiResponse<UserDTO>> detail(@PathVariable UUID id) throws ResourceNotFoundException {
        UserDTO role = userService.findByIdOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", role));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ALL','USER_UPDATE')")
    public ResponseEntity<ApiResponse<UserDTO>> update(@RequestBody RegisterRequest registerRequest) {
        UserDTO saved = userService.update(registerRequest.getId(), registerRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", saved));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ALL','USER_DELETE')")
    public ResponseEntity<ApiResponse<String>> delete(@RequestBody RoleDTO roleDTO) {
        UserDTO role = userService.findById(roleDTO.getId()).get();
        userService.delete(role.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", null));
    }
}
