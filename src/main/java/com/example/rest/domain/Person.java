package com.example.rest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String login;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    public Person(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.address = null;
    }

    public Person update(final Person newPerson) {
        this.password = Optional.ofNullable(newPerson.password).orElse(password);
        this.address = Optional.ofNullable(newPerson.address).orElse(address);
        return new Person(id, login, password, address);
    }
}
