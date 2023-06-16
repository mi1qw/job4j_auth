package com.example.rest.Exception;

import com.example.rest.service.PersonService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotExistingAccountValidator implements
        ConstraintValidator<NotExistingAccount, String> {

    private final PersonService personService;

    @Override
    public void initialize(final NotExistingAccount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String login,
                           final ConstraintValidatorContext context) {
        return personService.findByLogin(login).isEmpty();
    }
}
