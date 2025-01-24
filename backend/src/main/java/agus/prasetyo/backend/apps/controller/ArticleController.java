package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.apps.model.dto.ArticleDTO;
import agus.prasetyo.backend.apps.model.dto.RoleDTO;
import agus.prasetyo.backend.apps.model.request.ArticleRequest;
import agus.prasetyo.backend.apps.service.ArticleService;
import agus.prasetyo.backend.apps.service.RoleService;
import agus.prasetyo.backend.system.utils.ApiResponse;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ALL','ARTICLE_READ', 'ARTICLE_READ_ALL')")
    public ResponseEntity<ApiResponse<List<ArticleDTO>>> index(
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection,
            @RequestParam(required = false, defaultValue = "") String authorId,
            HttpServletRequest httpServletRequest) {
        List<ArticleDTO> lists = articleService.findArticle(
                getRoles(),
                sortBy,
                sortDirection,
                authorId,
                httpServletRequest
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", lists));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasAnyRole('ALL','ARTICLE_READ', 'ARTICLE_READ_ALL')")
    public ResponseEntity<ApiResponse<ArticleDTO>> detail(@PathVariable UUID id) throws ResourceNotFoundException {
        ArticleDTO role = articleService.findByIdOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", role));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ALL','ARTICLE_CREATE', 'ARTICLE_CREATE_ALL')")
    public ResponseEntity<ApiResponse<ArticleDTO>> register(@RequestBody ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        ArticleDTO saved = articleService.createArticle(getRoles(), articleRequest, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", saved));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ALL','ARTICLE_UPDATE', 'ARTICLE_UPDATE_ALL')")
    public ResponseEntity<ApiResponse<ArticleDTO>> update(@RequestBody ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        ArticleDTO saved = articleService.updateArticle(getRoles(), articleRequest.getId(), articleRequest, httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", saved));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ALL','ARTICLE_DELETE')")
    public ResponseEntity<ApiResponse<String>> delete(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO article = articleService.findById(articleDTO.getId()).get();
        articleService.delete(article.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", null));
    }

    private String getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
