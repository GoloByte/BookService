package golo.bookservice.core.mapping;

import golo.bookservice.api.dto.Charge;
import golo.bookservice.data.entity.ChargeEo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargeMapper {
    @Mapping(target = "timestamp",
            expression = "java(java.time.LocalDateTime.ofInstant(chargeEo.getTimestamp(),java.time.ZoneId.systemDefault()))")
    Charge map(ChargeEo chargeEo);
}
