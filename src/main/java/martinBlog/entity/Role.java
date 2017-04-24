package martinBlog.entity;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Martin on 4/18/2017.
 */

@Entity
@Table(name = "roles")
public class Role {

    private Integer id;

    private String name;

    private Set<User> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role() {
        this.users = new HashSet<>();
    }

    @ManyToMany(mappedBy = "roles")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Transient
    public String getSimpleName() {
        return StringUtils.capitalize(
                this.getName().substring(5).toLowerCase());
    }
}
