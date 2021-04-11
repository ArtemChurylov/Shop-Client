package com.example.shopclient.security.validation;

import com.example.shopclient.security.model.TempUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, TempUser> {
    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(TempUser tempUser, ConstraintValidatorContext context) {

        boolean isValid = true;

        // Just compare if password and confirm password are match
        if (tempUser.getPassword() != null && tempUser.getPassword().length() >= 6
                && tempUser.getConfirmPassword() != null && tempUser.getConfirmPassword().length() >= 6) {
            isValid = tempUser.getPassword().equals(tempUser.getConfirmPassword());
        }

        // If not, it`s throws an exception on filed
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword").addConstraintViolation();
        }

        return isValid;
    }
}
