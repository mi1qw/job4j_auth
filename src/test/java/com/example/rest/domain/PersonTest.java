package com.example.rest.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class PersonTest {

    @Test
    void updateOnlyFieldsNonNull() {
        Person pers = new Person(1, "login", "password");
        Person newPers = new Person(1, null, "111");
        Person updated = pers.update(newPers);
        log.info("{}", updated);
        assertThat(updated).usingRecursiveComparison()
                .isEqualTo(new Person(1, "login", "111"));
    }
}
