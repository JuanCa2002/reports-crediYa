package co.com.pragma.consumer.auth.mapper;

import co.com.pragma.consumer.auth.dto.ValidateResponseDTO;
import co.com.pragma.model.authentication.Authentication;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenticationMapper {

    Authentication toDomain(ValidateResponseDTO response);
}
