package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "business_organization", schema = "all_connected_businesses")
public class BusinessOrganization {
    @EmbeddedId
    private BusinessOrganizationId id;

    @MapsId("idBusiness")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_business", nullable = false)
    private Business business;
}