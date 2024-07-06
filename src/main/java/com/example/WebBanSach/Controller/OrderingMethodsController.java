package com.example.WebBanSach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderingMethodsController {

    @GetMapping("/ordering-methods")
    public String orderingMethods() {
        return "ordering-methods"; // This should match the Thymeleaf template name (ordering-methods.html)
    }
}
