package com.example.rest.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class PersonTest {

    @Test
    void patch() {
        Person personDB = new Person(
                1,
                "login",
                "password");
        Person newPerson = new Person(
                1,
                (String) null,
                "111");

        Person updated = personDB.patch(newPerson);
        log.info("\n{}\n{}", personDB, updated);

        assertThat(updated)
                .isEqualTo(new Person(
                        1,
                        "login",
                        null));
        assertThat(updated.isChanged()).isTrue();
        log.info("{} field", updated.isChanged());
    }
}
