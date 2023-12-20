package com.golobyte.bookservice.core.mapping;

import com.golobyte.bookservice.api.dto.Author;
import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.data.entity.AuthorEo;
import com.golobyte.bookservice.data.entity.BookEo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BookMapper {

    Book map(BookEo bookEo);

    /*
    explicit configuration for mapping of child
    ignore mapping of parent ( this ) to avoid circular mapping calls
    */
    @Mapping(target = "books", ignore = true)
    Author mapAuthor(AuthorEo authorEo);
}
