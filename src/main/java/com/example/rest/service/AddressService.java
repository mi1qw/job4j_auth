package com.example.rest.service;

import com.example.rest.domain.Address;
import com.example.rest.repository.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class AddressService {
    private final AddressRepository addressRepository;
    private final AtomicInteger atomId = new AtomicInteger();
//    final Map<Integer, Address> addressRepository = new HashMap<>(
//            Map.of(1, new Address(1, "Neverland", "Anyplace", "AnyStreet", "1")));

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address get(final int id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Address save(final Address address) {
//        int id = atomId.incrementAndGet();
//        address.setId(id);
        addressRepository.save(address);
//        addressRepository.put(id, new Address(id,
//                uadr.getCountry(),
//                uadr.getCity(),
//                uadr.getStreet(),
//                uadr.getHouse()));
//        log.info("{}", addressRepository.findById(id));
        return addressRepository.save(address);
    }

    public Address defaultAddress() {
        return addressRepository.findById(1).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
