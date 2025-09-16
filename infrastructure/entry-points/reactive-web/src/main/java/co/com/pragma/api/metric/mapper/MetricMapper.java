package co.com.pragma.api.metric.mapper;

import co.com.pragma.api.metric.dto.MetricResponseDTO;
import co.com.pragma.model.metric.Metric;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetricMapper {

    MetricResponseDTO toResponse(Metric domain);
}
