package golo.bookservice.core.mapping;

import golo.bookservice.api.dto.Book;
import golo.bookservice.data.entity.BookEo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BookMapper {
    Book map(BookEo bookEo);
}
