package com.example.rest.dto;

import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class UserMapperTest {
    private static final UserDto USER_DTO = new UserDto(
            "login",
            "password",
            "country",
            "city",
            "street",
            "house");
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Test
    void dtoToPerson() {
        Person person = userMapper.dtoToPerson(USER_DTO);
        assertThat(person).usingRecursiveComparison()
                .isEqualTo(new Person(0, "login",
                        "password", null));
        log.info("{}", person);
    }

    @Test
    void dtoToAddress() {
        Address address = addressMapper.dtoToAddress(USER_DTO);
        assertThat(address).usingRecursiveComparison()
                .isEqualTo(
                        new Address(0, "country", "city",
                                "street", "house"));
        log.info("{}", address);
    }
}