package com.golobyte.bookservice.core.mapping;

import com.golobyte.bookservice.api.dto.Author;
import com.golobyte.bookservice.data.entity.AuthorEo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author map(AuthorEo authorEo);
}
