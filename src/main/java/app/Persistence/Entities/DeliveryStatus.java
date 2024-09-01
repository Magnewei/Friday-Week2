package app.Persistence.Entities;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    PENDING,
    IN_TRANSIT,
    DELIVERED;

    // Go one level up. Stay on current level if package is DELIVERED.
    public DeliveryStatus getNextStatus() {
        return switch (this) {
            case PENDING -> IN_TRANSIT;
            case IN_TRANSIT -> DELIVERED;
            default -> this;
        };
    }
}
