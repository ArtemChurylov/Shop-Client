package com.example.shopclient.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    // Validation for phone number

    String message() default "This phone number is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
