package com.example.shopclient.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmDBPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmDBPassword {
    String message() default "Incorrect password.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
