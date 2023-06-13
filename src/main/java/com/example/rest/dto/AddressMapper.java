package com.example.rest.dto;

import com.example.rest.domain.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "address", ignore = true)
    Address dtoToAddress(UserDto userDto);
}
