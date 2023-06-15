package com.example.rest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@Data
//@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String login;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    public Person(int id, String login, String password,
                  boolean isChanged) {
        super(isChanged);
        this.id = id;
        this.login = login;
        this.password = password;
        this.address = null;
    }

    public Person(int id, String login, String password) {
        this(id, login, password, false);
    }

    public Person patch(final Person newPerson) {
        var isChanged = super.isChanged();
        var password_new = update(password, newPerson.password);
        var copy = new Person(
                id,
                login,
                password_new,
                super.isChanged());
        setChanged(isChanged);
        return copy;
    }
}
