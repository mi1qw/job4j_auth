package com.example.rest.dto;

import com.example.rest.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    Person dtoToPerson(UserDto userDto);
}
