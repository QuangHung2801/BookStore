package com.example.WebBanSach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReturnPolicyController {

    @GetMapping("/return-policy")
    public String returnPolicy() {
        return "return-policy"; // This should match the Thymeleaf template name (return-policy.html)
    }
}
