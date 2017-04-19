package martinBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import martinBlog.entity.User;

/**
 * Created by Martin on 4/18/2017.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
