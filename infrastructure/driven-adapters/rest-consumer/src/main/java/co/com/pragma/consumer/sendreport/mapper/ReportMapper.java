package co.com.pragma.consumer.sendreport.mapper;

import co.com.pragma.consumer.sendreport.dto.ReportRequestDTO;
import co.com.pragma.model.metric.Metric;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {

    @Mapping(target = "totalAmount", source = "totalApprovedAmount")
    @Mapping(target = "approvedProposals", source = "proposalApprovedQuantity")
    ReportRequestDTO toRequest(Metric domain);
}
