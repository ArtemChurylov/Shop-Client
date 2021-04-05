package com.example.shopclient.application.controller;

import com.example.shopclient.application.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final ProductService productService;

    public MainController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "application/mainPage";
    }

    @GetMapping("/fileTypeException")
    public String fileTypeException() {
        return "exception/fileTypeException";
    }
}
