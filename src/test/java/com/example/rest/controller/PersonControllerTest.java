package com.example.rest.controller;

import com.auth0.jwt.JWT;
import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import com.example.rest.dto.UserDto;
import com.example.rest.repository.AddressRepository;
import com.example.rest.repository.PersonRepository;
import com.example.rest.service.AddressService;
import com.example.rest.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.rest.security.JWTAuthenticationFilter.EXPIRATION_TIME;
import static com.example.rest.security.JWTAuthenticationFilter.SECRET;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class PersonControllerTest {
    @SpyBean
    private PersonRepository postRepository;
    @SpyBean
    private AddressRepository addressRepository;
    @Captor
    private ArgumentCaptor<Person> personCaptor;
    @Captor
    private ArgumentCaptor<Address> addressCaptor;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PersonService personService;

    private String jwtToken() {
        Authentication authentication =
                new TestingAuthenticationToken(
                        "admin",
                        "password",
                        "ROLE_USER");
        String token = JWT.create()
                .withSubject((String) authentication.getPrincipal())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        return token;
    }

    @Test
    void signUpUserDtoAndPatchWithAddressShouldSaveAndPatchAddress() {
        UserDto userDto = UserDto.builder()
                .login("loginTest")
                .password("passwordTest")
                .country("countryTest")
                .city("cityTest")
                .street("streetTest")
                .house("houseTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        var address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        var person = personCaptor.getValue();
        var prevPerson = person;
        log.info("\n\n\n{}\n{}", person, address);

        userDto = UserDto.builder()
                .login("loginTest")
                .password("password")
                .country("USA")
                .city("New York")
                /*.street("Some street")*/
                .house("111")
                .build();

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION, "Bearer " + jwtToken());

        var response1 = restTemplate.exchange("/person/",
                HttpMethod.PATCH,
                new HttpEntity<>(userDto, reqHeaders),
                Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);


        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        person = personCaptor.getValue();

        log.info("\n\n\n{}\n{}", person, address);

        assertThat(person.getAddress().getId()).isEqualTo(prevPerson.getAddress().getId());
        assertThat(person.getId()).isEqualTo(person.getId());
        assertThat(address).usingRecursiveComparison()
                .ignoringFields("isChanged")
                .isEqualTo(new Address(
                        address.getId(),
                        "USA",
                        "New York",
                        "streetTest",
                        "111"));
        prevPerson = person;

        var json = """
                {
                    "login": "loginTest",
                    "password": "passwordTest",
                    "street": "Wall Street"
                }
                """;
        reqHeaders = new HttpHeaders();
        reqHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        reqHeaders.add(AUTHORIZATION, "Bearer " + jwtToken());
        HttpEntity<?> entity1 = new HttpEntity<>(json, reqHeaders);

        response1 = restTemplate.exchange("/person/",
                HttpMethod.PATCH,
                entity1,
                Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        person = personCaptor.getValue();

        postRepository.deleteById(person.getId());
        addressRepository.deleteById(person.getAddress().getId());

        assertThat(person.getAddress().getId()).isEqualTo(prevPerson.getAddress().getId());
        assertThat(address).usingRecursiveComparison()
                .ignoringFields("isChanged", "isDefault")
                .isEqualTo(new Address(
                        address.getId(),
                        "USA",
                        "New York",
                        "Wall Street",
                        "111"));
        log.info("\n\n\n{}\n{}", person, address);
    }

    @Test
    void signUpUserDtoAndPatchWithAddressShouldSaveAndPatchAddress1() {
        UserDto userDto = UserDto.builder()
                .login("loginTest")
                .password("passwordTest")
                .country("countryTest")
                .city("cityTest")
                .street("streetTest")
                .house("houseTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        var address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        var person = personCaptor.getValue();
        var prevPerson = person;
        log.info("\n\n\n{}\n{}", person, address);

        var json = """
                {
                    "login": "loginTest",
                    "password": "passwordTest"
                }
                """;
        var reqHeaders = new HttpHeaders();
        reqHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        reqHeaders.add(AUTHORIZATION, "Bearer " + jwtToken());
        HttpEntity<?> entity1 = new HttpEntity<>(json, reqHeaders);

        var response1 = restTemplate.exchange("/person/",
                HttpMethod.PATCH,
                entity1,
                Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        person = personCaptor.getValue();

        postRepository.deleteById(person.getId());
        addressRepository.deleteById(person.getAddress().getId());

        assertThat(person.getAddress().getId()).isEqualTo(prevPerson.getAddress().getId());
        assertThat(address).usingRecursiveComparison()
                .ignoringFields("isChanged", "isDefault")
                .isEqualTo(new Address(
                        address.getId(),
                        "countryTest",
                        "cityTest",
                        "streetTest",
                        "houseTest"));
        log.info("\n\n\n{}\n{}", person, address);
    }

    @Test
    void signUpUserDtoWithoutAddressAndPatchWithAddressShouldSaveAndPatchAddress() {
        UserDto userDto = UserDto.builder()
                .login("loginTest")
                .password("passwordTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(postRepository).save(personCaptor.capture());
        var person = personCaptor.getValue();
        var prevPerson = person;
        log.info("\n\n\n{}", person);

        assertThat(person.getAddress().getId()).isEqualTo(addressService.defaultAddress().getId());

        /*--------------------------------------------------------------*/

        userDto = UserDto.builder()
                .login("loginTest")
                .password("password")
                .country("USA")
                .city("New York")
                /*.street("Some street")*/
                .house("111")
                .build();

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION, "Bearer " + jwtToken());

        var response1 = restTemplate.exchange("/person/",
                HttpMethod.PATCH,
                new HttpEntity<>(userDto, reqHeaders),
                Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(addressRepository, atLeastOnce()).save(addressCaptor.capture());
        var address = addressCaptor.getValue();
        verify(postRepository).save(personCaptor.capture());
        person = personCaptor.getValue();

        log.info("\n\n\n{}\n{}", person, address);

        int id = personService.findById(person.getId()).get()
                .getAddress().getId();
        postRepository.deleteById(person.getId());
        addressService.deleteById(id);
    }

    @Test
    void signUpUserDtoWithoutAddressAndPatchWithEmptyAddressShouldNotSaveAddress() {
        UserDto userDto = UserDto.builder()
                .login("loginTest")
                .password("passwordTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(postRepository).save(personCaptor.capture());
        var person = personCaptor.getValue();
        var prevPerson = person;
        log.info("\n\n\n{}", person);

        assertThat(person.getAddress().getId()).isEqualTo(addressService.defaultAddress().getId());

        /*--------------------------------------------------------------*/

        var json = """
                {
                    "login": "loginTest",
                    "password": "passwordTest"
                }
                """;
        var reqHeaders = new HttpHeaders();
        reqHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        reqHeaders.add(AUTHORIZATION, "Bearer " + jwtToken());
        HttpEntity<?> entity1 = new HttpEntity<>(json, reqHeaders);

        var response1 = restTemplate.exchange("/person/",
                HttpMethod.PATCH,
                entity1,
                Void.class);

        verify(postRepository).save(personCaptor.capture());
        person = personCaptor.getValue();

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(person.getAddress()).isEqualTo(addressService.defaultAddress());

        int addrid = personService.findById(person.getId()).get()
                .getAddress().getId();
        postRepository.deleteById(person.getId());
        addressService.deleteById(addrid);
    }
}
