package agus.prasetyo.backend.apps.service;

import agus.prasetyo.backend.apps.model.db.Article;
import agus.prasetyo.backend.apps.model.db.User;
import agus.prasetyo.backend.apps.model.dto.ArticleDTO;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.model.request.ArticleRequest;
import agus.prasetyo.backend.apps.repository.ArticleRepository;
import agus.prasetyo.backend.apps.repository.UserRepository;
import agus.prasetyo.backend.system.service.JwtService;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private  final UserRepository userRepository;

    public ArticleService(ArticleRepository roleRepository, ModelMapper modelMapper, JwtService jwtService, UserRepository userRepository) {
        this.articleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public List<ArticleDTO> findAll() {
        return articleRepository.findAll().stream().map(el -> modelMapper.map(el, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> findArticle(String roles, String sortBy, String sortDirection, String author,  HttpServletRequest httpServletRequest) {
        if (roles.contains("ROLE_ARTICLE_READ_ALL")) {
            return findAll(sortBy, sortDirection,author);
        } else {
            return findAllByAuthor(sortBy, sortDirection, httpServletRequest);
        }
    }

    @Cacheable(value = "all_article")
    public List<ArticleDTO> findAll(String sortBy, String sortDirection, String authorId) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        if (!authorId.equals("")) {
            User author = userRepository.findById(UUID.fromString(authorId)).get();
            return articleRepository.findAllByAuthor(author, sort).stream()
                    .map(el -> modelMapper.map(el, ArticleDTO.class))
                    .collect(Collectors.toList());
        } else {
            return articleRepository.findAll(sort).stream()
                    .map(el -> modelMapper.map(el, ArticleDTO.class))
                    .collect(Collectors.toList());
        }
    }

    @Cacheable(value = "all_article_by_owner")
    public List<ArticleDTO> findAllByAuthor(String sortBy, String sortDirection, HttpServletRequest httpServletRequest) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }


        User author = jwtService.getUserToken(httpServletRequest);
        return articleRepository.findAllByAuthor(author, sort).stream()
                .map(el -> modelMapper.map(el, ArticleDTO.class))
                .collect(Collectors.toList());
    }

    public ArticleDTO findByIdOrThrow(UUID id) throws ResourceNotFoundException {
        return articleRepository.findById(id)
                .map(el -> modelMapper.map(el, ArticleDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));
    }

    // @Cacheable(value = "detail_article", key = "#id")
    public Optional<ArticleDTO> findById(UUID id) {
        return articleRepository.findById(id).map(el -> modelMapper.map(el, ArticleDTO.class));
    }

    public ArticleDTO createArticle(String roles, ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        if (roles.contains("ROLE_ARTICLE_CREATE_ALL")) {
            return createByOther(articleRequest, httpServletRequest);
        } else {
            return create(articleRequest, httpServletRequest);
        }
    }

    public ArticleDTO createByOther(ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        ArticleDTO articleDTO = new ArticleDTO();
        Article article = new Article();
        if (!articleRequest.getAuthorId().equals("")) {
            User author = userRepository.findById(UUID.fromString(articleRequest.getAuthorId())).get();
            articleDTO.setAuthor(modelMapper.map(author, UserDTO.class));
            articleDTO.setTitle(articleRequest.getTitle());
            articleDTO.setContent(articleRequest.getContent());
            article = modelMapper.map(articleDTO, Article.class);
            article.setContributor(jwtService.getUserToken(httpServletRequest));
        } else {
            articleDTO = modelMapper.map(articleRequest, ArticleDTO.class);
            article = modelMapper.map(articleDTO, Article.class);
            article.setAuthor(jwtService.getUserToken(httpServletRequest));
        }

        Article saved = articleRepository.save(article);
        return modelMapper.map(saved, ArticleDTO.class);
    }

    public ArticleDTO create(ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        Article article = modelMapper.map(articleRequest, Article.class);
        article.setAuthor(jwtService.getUserToken(httpServletRequest));
        Article saved = articleRepository.save(article);
        return modelMapper.map(saved, ArticleDTO.class);
    }

    public ArticleDTO updateArticle(String roles, String id, ArticleRequest articleRequest, HttpServletRequest httpServletRequest) {
        if (roles.contains("ROLE_ARTICLE_UPDATE_ALL")) {
            // Check if contributor in
            Article article = articleRepository.findById(UUID.fromString(id)).get();
            if (article.getAuthor().getId() == jwtService.getUserToken(httpServletRequest).getId()) {
                System.out.println("Update Self ====================> ");
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setTitle(articleRequest.getTitle());
                articleDTO.setContent(articleRequest.getContent());
                return update(UUID.fromString(id), articleDTO);
            } else {
                System.out.println("Update Contributor ====================> ");
                article.setTitle(articleRequest.getTitle());
                article.setContent(articleRequest.getContent());
                article.setContributor(jwtService.getUserToken(httpServletRequest));
                Article updated = articleRepository.save(article);
                return modelMapper.map(updated, ArticleDTO.class);
            }
        } else {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setTitle(articleRequest.getTitle());
            articleDTO.setContent(articleRequest.getContent());
            return update(UUID.fromString(id), articleDTO);
        }
    }

    public ArticleDTO update(UUID id, ArticleDTO articleDTO) {
        return articleRepository.findById(id)
                .map(article -> {
                    if (articleDTO.getTitle() != null) {
                        article.setTitle(articleDTO.getTitle());
                    }
                    if (articleDTO.getContent() != null) {
                        article.setContent(articleDTO.getContent());
                    }
                    Article updated = articleRepository.save(article);
                    return modelMapper.map(updated, ArticleDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    public void delete(UUID id) {
        articleRepository.deleteById(id);
    }


}
