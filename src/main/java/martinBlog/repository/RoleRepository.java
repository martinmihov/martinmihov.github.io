package martinBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import martinBlog.entity.Role;

/**
 * Created by Martin on 4/18/2017.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
