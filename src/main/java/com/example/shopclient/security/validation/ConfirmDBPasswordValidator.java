package com.example.shopclient.security.validation;

import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmDBPasswordValidator implements ConstraintValidator<ConfirmDBPassword, String> {

    private final PasswordEncoder passwordEncoder;

    public ConfirmDBPasswordValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void initialize(ConfirmDBPassword constraint) {
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean isValid;

        try {
            Client client = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isValid = passwordEncoder.matches(password, client.getPassword());
        }catch (Exception e) {
            try {
                Seller seller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                isValid = passwordEncoder.matches(password, seller.getPassword());
            }catch (Exception e1) { return true; }
        }

        return isValid;
    }
}
