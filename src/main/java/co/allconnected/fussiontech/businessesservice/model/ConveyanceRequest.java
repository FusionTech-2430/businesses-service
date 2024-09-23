package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "conveyance_request", schema = "all_connected_businesses")
public class ConveyanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conveyance_request_id_gen")
    @SequenceGenerator(name = "conveyance_request_id_gen", sequenceName = "conveyance_request_id_conveyance_request_seq", allocationSize = 1)
    @Column(name = "id_conveyance_request", nullable = false)
    private Integer id;

    @ColumnDefault("false")
    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

}