package com.golobyte.bookservice.core.mapping;

import com.golobyte.bookservice.api.dto.Author;
import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.data.entity.AuthorEo;
import com.golobyte.bookservice.data.entity.ChargeEo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargeMapper {

    @Mapping(target = "importedOn",
            expression = "java(java.time.LocalDateTime.ofInstant(chargeEo.getImportedOn(),java.time.ZoneId.systemDefault()))")
    Charge map(ChargeEo chargeEo);

    /*
        explicit configuration for mapping of child
        ignore mapping of parent ( this ) to avoid circular mapping calls
    */
    @Mapping(target = "books", ignore = true)
    Author mapAuthor(AuthorEo authorEo);
}
