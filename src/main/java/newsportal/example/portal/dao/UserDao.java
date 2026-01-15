package newsportal.example.portal.dao;

import newsportal.example.portal.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbc;

    public UserDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public User findById(Long id) {
        String sql = "SELECT id, name, email FROM users WHERE id=?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setName(rs.getString("name"));
            u.setEmail(rs.getString("email"));
            return u;
        }, id);
    }

    public List<User> findAll() {
        String sql = "SELECT id, name, email FROM users";
        return jdbc.query(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setName(rs.getString("name"));
            u.setEmail(rs.getString("email"));
            return u;
        });
    }

    public int insert(User user) {
        String sql = "INSERT INTO users(name, email, photo) VALUES (?, ?, ?)";
        return jdbc.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getPhoto());
    }

    public int update(User u) {
        return jdbc.update(
                "UPDATE users SET name=?, email=?, photo=? WHERE id=?",
                u.getName(), u.getEmail(), u.getPhoto(), u.getId());
    }

    public int delete(Long id) {
        return jdbc.update("DELETE FROM users WHERE id=?", id);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setName(rs.getString("name"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            return u;
        }, email);
    }

}
