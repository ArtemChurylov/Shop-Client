package com.example.shopclient.security.controller;

import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.TempUser;
import com.example.shopclient.security.model.Seller;
import com.example.shopclient.security.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecurityController {

    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "security/loginPage";
    }

    @GetMapping("/registration/client")
    public String registrationClientPage(TempUser tempUser) {
        return "security/registrationClientPage";
    }

    @PostMapping("/registration/client")
    public String clientRegistration(@Valid TempUser tempUser, BindingResult result) {
        if (result.hasErrors()) return "security/registrationClientPage";
        userService.saveClient(tempUser);
        return "redirect:/";
    }

    @GetMapping("/registration/seller")
    public String registrationSellerPage(TempUser tempUser) {
        return "security/registrationSellerPage";
    }

    @PostMapping("/registration/seller")
    public String sellerRegistration(@Valid TempUser tempUser, BindingResult result) {
        if (result.hasErrors()) return "security/registrationSellerPage";
        userService.saveSeller(tempUser);
        return "redirect:/";
    }

    @GetMapping("/user/update")
    public String updateProfilePage(TempUser tempUser, Model model) {
        try {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", client);
        }catch (Exception e) {
            try {
                Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                model.addAttribute("user", seller);
            }catch (Exception e1) { throw new IllegalStateException(e1); }
        }
        return "security/updateProfile";
    }

    @PostMapping("/user/update")
    public String updateProfile(@Valid TempUser tempUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            try {
                Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                model.addAttribute("user", client);
            }catch (Exception e) {
                try {
                    Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    model.addAttribute("user", seller);
                }catch (Exception e1) { throw new IllegalStateException(e1); }
            }
            return "security/updateProfile";
        }

        userService.updateUser(tempUser);

        return "redirect:/profile";
    }

}
