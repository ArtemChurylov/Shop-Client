package com.example.shopclient.application.controller;

import com.example.shopclient.application.service.NotificationService;
import com.example.shopclient.application.service.ProductService;
import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private final ProductService productService;
    private final NotificationService notificationService;

    public MainController(ProductService productService, NotificationService notificationService) {
        this.productService = productService;
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        try {
            Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("notifications_number", seller.getNotificationCount());
        }catch (Exception e){}
        model.addAttribute("products", productService.getAllProducts());
        return "application/mainPage";
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("congratulationsPage")
    public String congratulationsPage(Model model) {
        Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("name", client.getName());
        model.addAttribute("surname", client.getSurname());
        return "/application/congratulationsPage";
    }

    @GetMapping("/profile")
    public String profileDetails(Model model) {
        try {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", client);
        }catch (Exception e) {
            try {
                Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                model.addAttribute("user", seller);
                model.addAttribute("notifications_number", seller.getNotificationCount());
            }catch (Exception e1) { throw new IllegalStateException(e1); }
        }
        return "application/profile";
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/notifications")
    public String notifications(Model model){
        Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("notifications", notificationService.getMyNotifications(seller.getId()));
        model.addAttribute("notifications_number", seller.getNotificationCount());
        return "application/notifications";
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/deleteNotification/{id}")
    public String deleteNotification(@PathVariable Long id) {
        Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.deleteNotification(id, seller);
        return "redirect:/notifications";
    }

    @GetMapping("/fileTypeException")
    public String fileTypeException() {
        return "exception/fileTypeException";
    }
}
