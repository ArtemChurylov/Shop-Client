package com.example.shopclient.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "This email is already taken.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
