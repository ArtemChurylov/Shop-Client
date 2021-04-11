package com.example.shopclient.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        if (s.startsWith("+")) {
            s = s.substring(1);
        }

        try {
            Long l = Long.parseLong(s);
        }catch (Exception e) {
            isValid = false;
        }

        return isValid;
    }
}
