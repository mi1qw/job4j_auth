package com.example.rest.repository;

import com.example.rest.domain.Person;
import org.springframework.data.repository.ListCrudRepository;

public interface PersonRepository extends ListCrudRepository<Person, Integer> {
}
