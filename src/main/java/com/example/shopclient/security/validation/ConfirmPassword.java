package com.example.shopclient.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {

    String message() default "Passwords dose`nt match.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
