package com.example.rest.repository;

import com.example.rest.domain.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PersonRepository extends ListCrudRepository<Person, Integer> {
    Person findByLogin(String username);

    @Transactional
    @Modifying
    @Query("update Person set login=?1, password=?2 where id=?3")
    int updateById(String login, String password, int id);

    @Transactional
    @Modifying
    @Query("delete from Person p where p.id = ?1")
    int deleteById(int id);
}
