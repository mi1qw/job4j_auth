package com.example.rest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String country;
    private String city;
    private String street;
    private String house;

    public Address(final int id, final String country,
                   final String city, final String street,
                   final String house, final boolean isChanged) {
        super(isChanged);
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public Address(final int id, final String country,
                   final String city, final String street,
                   final String house) {
        this(id, country, city, street, house, false);
    }

    public Address patch(final Address newAddress) {
        var isChanged = super.isChanged();
        var countryNew = update(country, newAddress.country);
        var cityNew = update(city, newAddress.city);
        var streetNew = update(street, newAddress.street);
        var houseNew = update(house, newAddress.house);
        var copy = new Address(
                id,
                countryNew,
                cityNew,
                streetNew,
                houseNew,
                super.isChanged());
        setChanged(isChanged);
        return copy;
    }

    public boolean isNotNull() {
        return country != null
                && city != null
                && street != null
                && house != null;
    }

    public boolean isChanged() {
        return super.isChanged();
    }
}
