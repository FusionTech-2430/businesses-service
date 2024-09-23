package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "business", schema = "all_connected_businesses")
public class Business {
    @Id
    @Column(name = "id_business", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "owner_id", nullable = false, length = 28)
    private String ownerId;

    @Column(name = "logo_url", length = 700)
    private String logoUrl;

}