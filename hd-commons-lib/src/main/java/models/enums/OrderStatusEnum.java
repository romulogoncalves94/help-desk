package models.enums;

import lombok.*;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public enum OrderStatusEnum {

    OPEN("Open"),
    CLOSED("Closed"),
    IN_PROGRESS("In Progress"),
    CANCELED("Canceled");

    @Getter
    private final String description;

    public static OrderStatusEnum toEnum(final String description) {
        return Arrays.stream(OrderStatusEnum.values())
                .filter(orderStatus -> orderStatus.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid OrderStatus description: " + description));
    }

}
