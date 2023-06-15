package com.example.rest.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class AddressTest {

    @Test
    void patch() {
        Address addressDB = new Address(
                10,
                "countryTest",
                "cityTest",
                "streetTest",
                "houseTest",
                false);
        Address newAddress = new Address(
                10,
                "USA",
                "New York",
                null,
                "1");

        Address updated = addressDB.patch(newAddress);
        log.info("\n{}\n{}", addressDB, updated);

        assertThat(updated)
                .isEqualTo(new Address(
                        10,
                        "USA",
                        "New York",
                        "streetTest",
                        "1"));
        assertThat(updated.isChanged()).isTrue();
        log.info("{} field", updated.isChanged());
    }
}