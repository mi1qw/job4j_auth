package com.example.rest.repository;

import com.example.rest.domain.Address;
import com.example.rest.domain.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PersonRepository extends ListCrudRepository<Person, Integer> {
    Optional<Person> findByLogin(String username);

    @Transactional
    @Modifying
    @Query("update Person set login=?1, password=?2, address=?3 where id=?4")
    int updateById(String login, String password, Address address, int id);

    @Transactional
    @Modifying
    @Query("delete from Person p where p.id = ?1")
    int deleteById(int id);
}
