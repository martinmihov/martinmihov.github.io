package martinBlog.repository;

import martinBlog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Martin on 4/18/2017.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findbyEmail(String email);
}
