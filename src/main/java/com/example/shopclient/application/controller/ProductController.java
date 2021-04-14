package com.example.shopclient.application.controller;

import com.example.shopclient.application.model.Address;
import com.example.shopclient.application.model.Product;
import com.example.shopclient.application.service.ProductService;
import com.example.shopclient.security.model.Client;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/add")
    public String addProductPage(Product product) {
        return "application/product/addProductPage";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file) {

        if (result.hasErrors()) return "application/product/addProductPage";

        if (file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png")) {
            productService.saveProduct(product, file);
        }else return "redirect:/fileTypeException";
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "application/product/showProduct";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/buy/{id}")
    public String buyProductPage(@PathVariable Long id, Address address, Model model) {
        model.addAttribute("id", id);
        return "application/product/buyProductPage";
    }

    @PostMapping("/buy/{id}")
    public String buyProduct(@PathVariable Long id, @Valid Address address, BindingResult result) {
        if (result.hasErrors()) return "application/product/buyProductPage";
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        productService.buyProduct(id, address, client);
        return "redirect:/";
    }
}
