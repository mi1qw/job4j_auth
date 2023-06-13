package com.example.rest.service;

import com.example.rest.Exception.AddressNotFoundException;
import com.example.rest.domain.Address;
import com.example.rest.dto.AddressMapper;
import com.example.rest.dto.UserDto;
import com.example.rest.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private Address defaultAddress;

    @PostConstruct
    public void init() {
        this.defaultAddress = addressRepository
                .findByCountryAndCity("Neverland", "Anyplace")
                .orElseThrow(() -> new AddressNotFoundException("Address Not Found"));
    }


    public AddressService(final AddressRepository addressRepository,
                          final AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public Address get(final int id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Address save(final Address address) {
        if (address.getId() == defaultAddress.getId()) {
            address.setId(0);
        }
        return addressRepository.save(address);
    }

    @Transactional
    public Address save(final UserDto userDto) {
        return Optional.of(addressMapper.dtoToAddress(userDto))
                .filter(Address::isNotNull)
                .map(this::save)
                .orElse(defaultAddress);
    }

    public Optional<Address> findById(final int id) {
        if (id == defaultAddress.getId()) {
            return Optional.of(defaultAddress);
        }
        return addressRepository.findById(id);
    }

    public boolean update(final Address address) {
        if (address.getId() == defaultAddress.getId()) {
            return false;
        }
        return addressRepository.updateById(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getId()
        ) > 0;
    }

    public Address patch(final int id, final Address addressDto) {
        return findById(id)
                .map(a -> a.patch(addressDto))
                .filter(Address::isChanged)
                .map(this::save)
                .orElse(defaultAddress);
    }

    public boolean deleteById(final int id) {
        if (id == defaultAddress.getId()) {
            return false;
        }
        return addressRepository.deleteById(id) > 0;
    }

    public Address defaultAddress() {
        return defaultAddress;
    }
}
