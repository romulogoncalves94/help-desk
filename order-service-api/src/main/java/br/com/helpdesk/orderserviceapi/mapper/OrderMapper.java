package br.com.helpdesk.orderserviceapi.mapper;

import br.com.helpdesk.orderserviceapi.entities.Order;
import models.enums.OrderStatusEnum;
import models.requests.CreatedOrderRequest;
import org.mapstruct.*;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    Order fromRequest(CreatedOrderRequest request);

    @Named("mapStatus")
    default OrderStatusEnum mapStatus(final String status) {
        return OrderStatusEnum.toEnum(status);
    }

}
