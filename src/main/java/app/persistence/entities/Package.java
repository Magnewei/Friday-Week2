package app.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "packages")
@EqualsAndHashCode
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "tracking_number", unique = false, nullable = false)
    private String trackingNumber;

    @Column(name = "sender_name", unique = false, nullable = false)
    private String senderName;

    @Column(name = "receiver_name", unique = false, nullable = false)
    private String receiverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", unique = false, nullable = false)
    private DeliveryStatus deliveryStatus;

    public enum DeliveryStatus {
        PENDING,
        IN_TRANSIT,
        DELIVERED;
    }

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    private void onCreate() {
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
