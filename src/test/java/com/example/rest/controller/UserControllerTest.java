package com.example.rest.controller;

import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import com.example.rest.dto.UserDto;
import com.example.rest.repository.AddressRepository;
import com.example.rest.repository.PersonRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
class UserControllerTest {
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
                .login("loginTest")
                .password("passwordTest")
                .build();
        ResponseEntity<UserDto> response = restTemplate
                .postForEntity("/users/sign-up", userDto, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(postRepository).save(personCaptor.capture());
        Person person = personCaptor.getValue();

        assertThat(person.getLogin()).isEqualTo("loginTest");
        assertThat(postRepository.findById(person.getId()).get().getLogin())
                .isEqualTo("loginTest");
        assertThat(person.getAddress().getCountry()).isEqualTo("Neverland");
        postRepository.deleteById(person.getId());
    }

    @Test
    void signUpUserDtoWithAddressShouldSaveAddress() {
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


        assertThat(person.getLogin()).isEqualTo("loginTest");
        assertThat(postRepository.findById(person.getId()).get().getLogin())
                .isEqualTo("loginTest");
        assertThat(person.getAddress().getCountry()).isEqualTo("countryTest");

        assertThat(address).satisfies(
                a -> assertThat(a.getCountry()).isEqualTo("countryTest"),
                a -> assertThat(a.getCity()).isEqualTo("cityTest"),
                a -> assertThat(addressRepository.findById(address.getId()).get().getCountry())
                        .isEqualTo("countryTest"));

        postRepository.deleteById(person.getId());
        addressRepository.deleteById(address.getId());
    }
}