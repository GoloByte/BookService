package com.golobyte.bookservice.core.mapping;

import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.data.entity.ChargeEo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargeMapper {
    @Mapping(target = "importedOn",
            expression = "java(java.time.LocalDateTime.ofInstant(chargeEo.getImportedOn(),java.time.ZoneId.systemDefault()))")
    Charge map(ChargeEo chargeEo);
}
