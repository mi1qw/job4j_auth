package com.example.rest.controller;

import com.example.rest.Exception.CustomValidator;
import com.example.rest.domain.Person;
import com.example.rest.dto.AddressMapper;
import com.example.rest.dto.UserDto;
import com.example.rest.dto.UserMapper;
import com.example.rest.repository.PersonRepository;
import com.example.rest.service.AddressService;
import com.example.rest.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final AddressService addressService;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final PersonService personService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(final @RequestBody UserDto userDto) {
        validator.check(userDto);
        personService.save(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return users.findAll();
    }
}
