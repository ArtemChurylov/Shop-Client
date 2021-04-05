package com.example.shopclient.security.validation;

import com.example.shopclient.security.model.Client;
import com.example.shopclient.security.model.Seller;
import com.example.shopclient.security.model.TempUser;
import com.example.shopclient.security.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, TempUser> {

    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(TempUser tempUser, ConstraintValidatorContext context) {

        boolean isValid = true;


        // This is logic of validation email for updating User.
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {

            try {
                Client authenticatedClient = (Client) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                if (tempUser.getEmail().equals(authenticatedClient.getEmail())) {
                    return true;
                }else {
                    if (userService.getAllClients().stream().filter(client -> !client.getEmail().equals(authenticatedClient.getEmail()))
                            .collect(Collectors.toList()).stream().noneMatch(client -> client.getEmail().equals(authenticatedClient.getEmail())) &&
                            userService.getAllSellers().stream().noneMatch(seller -> seller.getEmail().equals(authenticatedClient.getEmail()))) {
                        isValid = true;
                    } else isValid = false;
                }
            } catch (Exception e) {
                try {
                    Seller authenticatedSeller = (Seller) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    if (tempUser.getEmail().equals(authenticatedSeller.getEmail())) {
                        return true;
                    }else {
                        if (userService.getAllSellers().stream().filter(seller -> !seller.getEmail().equals(authenticatedSeller.getEmail()))
                                .collect(Collectors.toList()).stream().noneMatch(seller -> seller.getEmail().equals(authenticatedSeller.getEmail())) &&
                                userService.getAllClients().stream().noneMatch(client -> client.getEmail().equals(authenticatedSeller.getEmail()))) {
                            isValid = true;
                        } else isValid = false;
                    }
                }catch (Exception e1) {
                    throw new IllegalStateException(e1);
                }
            }

        }

        // Compare email from form with emails from DB.
        if (tempUser.getEmail() != null){
            if (userService.getAllClients().stream().noneMatch(client -> client.getEmail().equals(tempUser.getEmail())) &&
                    userService.getAllSellers().stream().noneMatch(seller -> seller.getEmail().equals(tempUser.getEmail()))) {
                isValid = true;
            } else isValid = false;
        }

        // If not valid, throws an exception on field.
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email").addConstraintViolation();
        }

        return isValid;
    }
}
