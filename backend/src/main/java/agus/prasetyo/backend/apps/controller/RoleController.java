package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.apps.model.dto.ArticleDTO;
import agus.prasetyo.backend.apps.model.dto.RoleDTO;
import agus.prasetyo.backend.apps.service.RoleService;
import agus.prasetyo.backend.system.utils.ApiResponse;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ALL','ROLE_READ')")
    public ResponseEntity<ApiResponse<List<RoleDTO>>> index(@RequestParam(required = false, defaultValue = "created_at") String sortBy,
                                                            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {
        List<RoleDTO> lists = roleService.findAll(sortBy, sortDirection);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", lists));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ALL','ROLE_DETAIL')")
    public ResponseEntity<ApiResponse<RoleDTO>> detail(@PathVariable UUID id) throws ResourceNotFoundException {
        RoleDTO role = roleService.findByIdOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", role));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ALL','ROLE_CREATE')")
    public ResponseEntity<ApiResponse<RoleDTO>> register(@RequestBody RoleDTO roleDTO) {
        RoleDTO saved = roleService.create(roleDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", saved));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ALL','ROLE_UPDATE')")
    public ResponseEntity<ApiResponse<RoleDTO>> update(@RequestBody RoleDTO roleDTO) {
        RoleDTO saved = roleService.update(roleDTO.getId(), roleDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", saved));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ALL','ROLE_DELETE')")
    public ResponseEntity<ApiResponse<String>> delete(@RequestBody RoleDTO roleDTO) {
        RoleDTO role = roleService.findById(roleDTO.getId()).get();
        roleService.delete(role.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", null));
    }
}
