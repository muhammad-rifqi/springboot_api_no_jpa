package newsportal.example.portal.controller;

import newsportal.example.portal.dao.UserDao;
import newsportal.example.portal.model.User;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;
// import java.util.HashMap;

import newsportal.example.portal.util.HashUtil;
import newsportal.example.portal.util.JwtUtil;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDao dao;

    public UserController(UserDao dao) {
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
        User user = dao.findById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "success", false,
                            "message", "User tidak ditemukan"));
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", user));
    }

    // ================= CREATE =================
    @PostMapping(value = "/store", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("photo") MultipartFile photo) {

        Map<String, Object> res = new HashMap<>();

        try {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + filename);
            Files.write(filePath, photo.getBytes());

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPhoto(filename);

            dao.insert(user);

            res.put("status", true);
            res.put("message", "Data berhasil disimpan");
            res.put("filename", filename);

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("status", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    // ================= UPDATE =================
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) MultipartFile photo) {

        Map<String, Object> res = new HashMap<>();

        try {
            User user = dao.findById(id);

            user.setName(name);
            user.setEmail(email);

            if (photo != null && !photo.isEmpty()) {
                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + filename);
                Files.write(filePath, photo.getBytes());

                user.setPhoto(filename);
            }
            dao.update(user);

            res.put("status", true);
            res.put("message", "Data berhasil diupdate");

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("status", false);
            res.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/destroy/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            User user = dao.findById(id);
            if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                Path photoPath = Paths.get("uploads", user.getPhoto());
                Files.deleteIfExists(photoPath);
            }
            dao.delete(id);

            res.put("status", true);
            res.put("message", "Data dan foto berhasil dihapus");

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("status", false);
            res.put("message", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(res);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletResponse response) {

        Map<String, Object> res = new HashMap<>();

        try {
            User user = dao.findByEmail(email);
            String md5Password = HashUtil.md5(password);
            if (!user.getPassword().equals(md5Password)) {
                res.put("success", false);
                res.put("message", "Email atau password salah");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            String token = JwtUtil.generateToken(user.getEmail());

            res.put("success", true);
            res.put("message", "Login berhasil");
            res.put("token", token);
            res.put("data", user);

            return ResponseEntity.ok(res);

        } catch (EmptyResultDataAccessException e) {
            res.put("success", false);
            res.put("message", "User tidak ditemukan");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile(
            @RequestHeader("Authorization") String authHeader) {

        Map<String, Object> res = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            String email = JwtUtil.getEmailFromToken(token);
            User user = dao.findByEmail(email);
            res.put("success", true);
            res.put("data", user);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.put("success", false);
            res.put("message", "Token tidak valid / expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        }
    }

}
