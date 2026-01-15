package newsportal.example.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserViewController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/detail/{id}")
    public String detail() {
        return "detail";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }
}
