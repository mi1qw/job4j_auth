package com.example.rest.controller;

import com.example.rest.Exception.CustomValidator;
import com.example.rest.domain.Person;
import com.example.rest.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final PersonRepository users;
    private final PasswordEncoder encoder;
    private final CustomValidator validator;

    @PostMapping("/sign-up")
    public void signUp(final @RequestBody Person person) {
        validator.check(person);
        person.setPassword(encoder.encode(person.getPassword()));
        users.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return users.findAll();
    }
}
