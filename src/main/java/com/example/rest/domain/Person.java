package com.example.rest.domain;

import com.example.rest.Exception.NotExistingAccount;
import com.example.rest.Exception.Operation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)"
            + "+[a-zA-Z]{2,6}$", message = "Invalid email address",
            groups = Operation.OnCreate.class)
    @NotExistingAccount(groups = Operation.OnCreate.class)
    private String login;
    @Length(min = 6, message = "Password must be at least 6 characters",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    public Person(final int id, final String login,
                  final String password, final boolean isChanged) {
        super(isChanged);
        this.id = id;
        this.login = login;
        this.password = password;
        this.address = null;
    }

    public Person(final int id, final String login,
                  final String password) {
        this(id, login, password, false);
    }

    public Person patch(final Person newPerson) {
        var isChanged = super.isChanged();
        var passwordNew = update(password, newPerson.password);
        var copy = new Person(
                id,
                login,
                passwordNew,
                super.isChanged());
        setChanged(isChanged);
        return copy;
    }
}
