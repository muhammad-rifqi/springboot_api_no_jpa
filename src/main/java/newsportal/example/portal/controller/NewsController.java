package newsportal.example.portal.controller;

import newsportal.example.portal.dao.NewsDao;
import newsportal.example.portal.model.News;

// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

      private final NewsDao dao;

    public NewsController(NewsDao dao) {
        this.dao = dao;
    }

     // ================= LIST =================
    @GetMapping
    public ResponseEntity<Map<String, Object>> all() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", dao.findAll());
        return ResponseEntity.ok(response);
    }

       // ================= DETAIL =================
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable Long id) {
        News news = dao.findById(id);
        if (news == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "success", false,
                            "message", "User tidak ditemukan"));
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", news));
    }
    
}
