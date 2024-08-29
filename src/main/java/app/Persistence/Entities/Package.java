package app.Persistence.Entities;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true,nullable = false)
    private long id;

    @Column(name = "tracking_number",unique = false, nullable = false)
    private String trackingNumber;

    @Column(name = "sender_name",unique = false, nullable = false)
    private String senderName;

    @Column(name = "receiver_name",unique = false, nullable = false)
    private String receiverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status",unique = false, nullable = false)
    private DeliveryStatus deliveryStatus;
}
