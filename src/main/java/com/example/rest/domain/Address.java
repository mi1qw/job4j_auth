package com.example.rest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
//@AllArgsConstructor
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

    public Address(int id, String country,
                   String city, String street, String house,
                   boolean isChanged) {
        super(isChanged);
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    public Address(int id, String country,
                   String city, String street, String house) {
        this(id, country, city, street, house, false);
    }

    public Address patch(final Address newAddress) {
        var isChanged = super.isChanged();
        var country_new = update(country, newAddress.country);
        var city_new = update(city, newAddress.city);
        var street_new = update(street, newAddress.street);
        var house_new = update(house, newAddress.house);
        var copy = new Address(
                id,
                country_new,
                city_new,
                street_new,
                house_new,
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
