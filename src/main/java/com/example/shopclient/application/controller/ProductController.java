package com.example.shopclient.application.controller;

import com.example.shopclient.application.model.Address;
import com.example.shopclient.application.model.Product;
import com.example.shopclient.application.service.ProductService;
import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
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
        try {
            Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("notifications_number", seller.getNotifications().size());
        }catch (Exception e){}
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
        return "redirect:/congratulationsPage";
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/myProducts")
    public String myProducts(Model model) {
        Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("notifications_number", seller.getNotifications().size());
        model.addAttribute("products", productService.getMyProducts(seller.getId()));
        return "application/product/myProducts";
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/{id}/edit")
    public String editProductPage(@PathVariable Long id, Model model) {
        Product dbProduct = productService.getProductById(id);
        model.addAttribute("product", dbProduct);
        return "application/product/editProductPage";
    }

    @PostMapping("/{id}/edit")
    public String editProduct(@Valid Product product, BindingResult result, MultipartFile file, @PathVariable Long id) {
        if (result.hasErrors()) return "application/product/editProductPage";
        if (!file.getOriginalFilename().equals("")) {
            if (file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png")) {
                productService.updateProduct(product, file, id);
            } else return "redirect:/fileTypeException";
        }else productService.updateProduct(product, file, id);

        return "redirect:/product/myProducts";
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/product/myProducts";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/myOrders")
    public String showClientOrders(Model model) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("orders", productService.getMyOrders(client.getId()));
        return "application/product/myOrders";
    }
}
