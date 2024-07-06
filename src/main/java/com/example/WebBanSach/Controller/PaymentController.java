package com.example.WebBanSach.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @GetMapping("/payment-methods")
    public String showPaymentMethods() {
        return "payment-methods"; // Trả về tên của tệp HTML mà bạn đã tạo
    }
}