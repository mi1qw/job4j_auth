package com.example.rest.controller;

import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import com.example.rest.dto.UserDto;
import com.example.rest.repository.AddressRepository;
import com.example.rest.repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
class UserControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
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


    @Test
    void signUpUserDtoWithoutAddress() {
        UserDto userDto = UserDto.builder()
                .login("abc@mail.com")
                .password("passwordTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(postRepository).save(personCaptor.capture());
        Person person = personCaptor.getValue();

        assertThat(person.getLogin()).isEqualTo("abc@mail.com");
        assertThat(postRepository.findById(person.getId()).get().getLogin())
                .isEqualTo("abc@mail.com");
        assertThat(person.getAddress().getCountry()).isEqualTo("Neverland");
        postRepository.deleteById(person.getId());
    }

    @Test
    void signUpUserDtoWithAddressShouldSaveAddress() {
        UserDto userDto = UserDto.builder()
                .login("abc@mail.com")
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


        assertThat(person.getLogin()).isEqualTo("abc@mail.com");
        assertThat(postRepository.findById(person.getId()).get().getLogin())
                .isEqualTo("abc@mail.com");
        assertThat(person.getAddress().getCountry()).isEqualTo("countryTest");

        assertThat(address).satisfies(
                a -> assertThat(a.getCountry()).isEqualTo("countryTest"),
                a -> assertThat(a.getCity()).isEqualTo("cityTest"),
                a -> assertThat(addressRepository.findById(address.getId()).get().getCountry())
                        .isEqualTo("countryTest"));

        postRepository.deleteById(person.getId());
        addressRepository.deleteById(address.getId());
    }

    @Test
    void signUpUserDtoWithInvalidEmail() {
        UserDto userDto = UserDto.builder()
                .login("ab@.com")
                .password("password")
                .build();
        var response = restTemplate.postForEntity("/users/sign-up", userDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(jsonToMap(response.getBody())).satisfies(
                r -> assertThat(r.get("message")).contains("Some of fields empty or wrong"),
                r -> assertThat(r.get("details")).contains("Invalid email address"));
    }

    @Test
    void signUpUserDtoWithInvalidpassword() throws JsonProcessingException {
        UserDto userDto = UserDto.builder()
                .login("abc@mail.com")
                .password("pass")
                .build();
        var response = restTemplate.postForEntity("/users/sign-up", userDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(jsonToMap(response.getBody())).satisfies(
                r -> assertThat(r.get("message")).contains("Some of fields empty or wrong"),
                r -> assertThat(r.get("details"))
                        .contains("Password must be at least 6 characters"));
    }

    @Test
    void signUpUserDtoWithExistingEmail() {
        UserDto userDto = UserDto.builder()
                .login("admin@mail.com")
                .password("password")
                .build();
        var response = restTemplate.postForEntity("/users/sign-up", userDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var map = jsonToMap(response.getBody());
        System.out.println(map);
        assertThat(jsonToMap(response.getBody())).satisfies(
                r -> assertThat(r.get("message")).contains("Some of fields empty or wrong"),
                r -> assertThat(r.get("details"))
                        .contains("An account with this email already exists"));
    }

    @Test
    void signUpUserDtoWithInvalidCountry() {
        UserDto userDto = UserDto.builder()
                .login("1admin@mail.com")
                .password("password")
                .country("")
                .build();
        var response = restTemplate.postForEntity("/users/sign-up", userDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        var map = jsonToMap(response.getBody());
        System.out.println(map);
        assertThat(jsonToMap(response.getBody())).satisfies(
                r -> assertThat(r.get("message")).contains("Some of fields empty or wrong"),
                r -> assertThat(r.get("details")).contains("Fields must be not empty"));
    }

    private Map<String, String> jsonToMap(final Object json) {
        Map<String, String> map = null;
        try {
            return OBJECT_MAPPER.readValue((String) json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
