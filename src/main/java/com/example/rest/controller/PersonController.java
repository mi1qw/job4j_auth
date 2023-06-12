package com.example.rest.controller;

import com.example.rest.Exception.CustomValidator;
import com.example.rest.Exception.PersonNotFoundException;
import com.example.rest.domain.Person;
import com.example.rest.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final CustomValidator validator;

    @GetMapping("/")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(final @PathVariable int id) {
        validator.check(() -> id == 0, "Wrong id");
        Person person = personService.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person Not Found"));
        return ResponseEntity.ok(person);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(final @RequestBody Person person) {
        validator.check(person);
        var pers = personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(pers);
    }

    @PutMapping("/")
    public ResponseEntity<?> update(final @RequestBody Person person) {
        validator.check(person);
        if (personService.update(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("error updating person");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(final @PathVariable int id) {
        validator.check(() -> id == 0, "Wrong id");
        if (personService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("error deleting person");
    }
}
