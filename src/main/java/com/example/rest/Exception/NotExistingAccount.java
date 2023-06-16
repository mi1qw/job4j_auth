package com.example.rest.Exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotExistingAccountValidator.class)
public @interface NotExistingAccount {
    String message() default "An account with this email already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
