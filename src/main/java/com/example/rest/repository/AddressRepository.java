package com.example.rest.repository;

import com.example.rest.domain.Address;
import org.springframework.data.repository.CrudRepository;


public interface AddressRepository extends CrudRepository<Address, Integer> {
}
