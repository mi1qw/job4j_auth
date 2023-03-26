package com.example.rest.service;

import com.example.rest.domain.Person;
import com.example.rest.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository repo;
    private final PasswordEncoder encoder;

    public List<Person> findAll() {
        return repo.findAll();
    }

    public Optional<Person> findById(final int id) {
        return repo.findById(id);
    }

    public Person save(final Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return repo.save(person);
    }

    public boolean update(final Person person) {
        return repo.updateById(person.getLogin(),
                person.getPassword(), person.getId()) > 0;
    }

    public boolean deleteById(final int id) {
        return repo.deleteById(id) > 0;
    }
}
