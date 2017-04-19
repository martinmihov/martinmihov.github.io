package martinBlog.repository;

import martinBlog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Martin on 4/19/2017.
 */
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
