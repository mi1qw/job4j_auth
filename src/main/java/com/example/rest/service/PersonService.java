package com.example.rest.service;

import com.example.rest.Exception.PersonNotFoundException;
import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import com.example.rest.dto.AddressMapper;
import com.example.rest.dto.UserDto;
import com.example.rest.dto.UserMapper;
import com.example.rest.repository.PersonRepository;
import jakarta.transaction.Transactional;
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
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final AddressService addressService;

    public List<Person> findAll() {
        return repo.findAll();
    }

    public Optional<Person> findById(final int id) {
        return repo.findById(id);
    }

    public Optional<Person> findByLogin(final String login) {
        return repo.findByLogin(login);
    }

    @Transactional
    public Person save(final UserDto userDto) {
        var person = userMapper.dtoToPerson(userDto);
        var address = addressService.save(userDto);
        person.setAddress(address);
        person.setPassword(encoder.encode(person.getPassword()));
        return repo.save(person);
    }

    public Person save(final Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        return repo.save(person);
    }

    public boolean update(final Person person) {
        if (person.isChanged()) {
            person.setPassword(encoder.encode(person.getPassword()));
        }
        return repo.updateById(
                person.getLogin(),
                person.getPassword(),
                person.getAddress(),
                person.getId()
        ) > 0;
    }

    public boolean deleteById(final int id) {
        return repo.deleteById(id) > 0;
    }

    @Transactional
    public boolean patch(final UserDto userDto) {
        Person prsDto = userMapper.dtoToPerson(userDto);
        Address adrDto = addressMapper.dtoToAddress(userDto);

        Person personDB = findByLogin(prsDto.getLogin())
                .orElseThrow(() -> new PersonNotFoundException("Person Not Found"));
        var idAddrBD = personDB.getAddress().getId();
        Address newAddress = addressService.patch(idAddrBD, adrDto);

        Person patched = personDB.patch(prsDto);
        patched.setAddress(newAddress);
        return update(patched);
    }
}
