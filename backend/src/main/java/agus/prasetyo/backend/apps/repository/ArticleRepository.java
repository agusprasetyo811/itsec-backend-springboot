package agus.prasetyo.backend.apps.repository;

import agus.prasetyo.backend.apps.model.db.Article;
import agus.prasetyo.backend.apps.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findAllByAuthor(User author, Sort sort);
}
