package newsportal.example.portal.dao;

import newsportal.example.portal.model.News;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class NewsDao {

    private final JdbcTemplate jdbc;

    public NewsDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

   public List<News> findAll() {
        String sql = "SELECT * FROM news";
        return jdbc.query(sql, (rs, rowNum) -> {
            News n = new News();
            n.setId(rs.getLong("id"));
            n.setTitle(rs.getString("title"));
            n.setDescription(rs.getString("description"));
            return n;
        });
    }

    public News findById(Long id) {
        String sql = "SELECT * FROM news WHERE id=?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            News n = new News();
            n.setId(rs.getLong("id"));
            n.setTitle(rs.getString("title"));
            n.setDescription(rs.getString("description"));
            return n;
        }, id);
    }

}
