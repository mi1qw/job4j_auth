package com.example.rest.repository;

import com.example.rest.domain.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface AddressRepository extends CrudRepository<Address, Integer> {
    Optional<Address> findByCountryAndCity(String country, String city);

    @Transactional
    @Modifying
    @Query("update Address set country=?1, city=?2, street=?3, house=?4 where id=?5")
    int updateById(String country, String city, String street, String house, int id);

    @Transactional
    @Modifying
    @Query("delete from Address a where a.id = ?1")
    int deleteById(int id);

}
