package com.example.WebBanSach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShippingMethodsController {

    @GetMapping("/shipping-methods")
    public String shippingMethods() {
        return "shipping-methods"; // This should match the Thymeleaf template name (shipping-methods.html)
    }
}
