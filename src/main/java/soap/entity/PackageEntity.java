package soap.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "packages")
public class PackageEntity {
    @Id
    @Column(name = "tracking_id", length = 50)
    private String trackingId;

    @Column(nullable = false)
    private String status;

    @Column(name = "current_location")
    private String currentLocation;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackingEventEntity> history;
}
