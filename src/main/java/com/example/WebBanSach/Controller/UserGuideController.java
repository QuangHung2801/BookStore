package com.example.WebBanSach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserGuideController {

    @GetMapping("/user-guide")
    public String userGuide() {
        return "user-guide"; // This should match the Thymeleaf template name (user-guide.html)
    }
}
