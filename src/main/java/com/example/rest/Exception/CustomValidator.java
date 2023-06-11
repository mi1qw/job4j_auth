package com.example.rest.Exception;

import com.example.rest.domain.Person;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CustomValidator {
    public void check(final Supplier<Boolean> s, final String message, final Object... ob) {
        if (s.get()) {
            if (ob.length > 0) {
                throw new EmptyArgumentException(
                        String.format("%s in class %s", message,
                                ob[0].getClass().getSimpleName()));
            } else {
                throw new EmptyArgumentException(message);
            }
        }
    }

    public void check(final Person person) {
        String login = person.getLogin();
        String password = person.getPassword();
        check(() -> login == null || login.isEmpty()
                        || password == null || password.isEmpty(),
                "Username and password mustn't be empty",
                person);
    }
}
