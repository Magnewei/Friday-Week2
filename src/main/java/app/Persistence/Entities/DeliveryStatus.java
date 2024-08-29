package app.Persistence.Entities;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    PENDING(1, "Package pending."),
    IN_TRANSIT(2, "Package in transit."),
    DELIVERED(3, "Package delivered.");

    private final int level;
    private final String description;

    DeliveryStatus(int level, String description) {
        this.level = level;
        this.description = description;
    }


    // Go one level up. Stay on current level if package is DELIVERED.
    public DeliveryStatus getNextStatus() {
        return switch (this) {
            case PENDING -> IN_TRANSIT;
            case IN_TRANSIT -> DELIVERED;
            default -> this;
        };
    }
}
