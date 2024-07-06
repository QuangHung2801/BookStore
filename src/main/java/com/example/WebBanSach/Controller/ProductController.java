package com.example.WebBanSach.Controller;

import com.example.WebBanSach.entity.Product;
import com.example.WebBanSach.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

import java.util.List;


@Controller
public class    ProductController {
    private final String UPLOAD_DIR = "src/main/resources/static/Admin/img/products/";

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "Customer/index";
    }

    @GetMapping("/category")
    public String showCategory(Model model) {
        return "Customer/category";
    }

    @GetMapping("/product")
    public String showProduct(Model model) {
        return "Customer/product";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        return "Customer/contact";
    }
    @GetMapping("/post")
    public String showPost(Model model) {
        return "Customer/post";
    }


}
