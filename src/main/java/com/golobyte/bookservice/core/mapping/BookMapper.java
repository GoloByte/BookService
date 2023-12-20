package com.golobyte.bookservice.core.mapping;

import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.data.entity.BookEo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookMapper {

    Book map(BookEo bookEo);
}
