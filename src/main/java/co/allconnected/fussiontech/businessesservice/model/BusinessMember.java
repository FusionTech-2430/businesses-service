package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "business_member", schema = "all_connected_businesses")
public class BusinessMember {
    @EmbeddedId
    private BusinessMemberId id;

    @MapsId("idBusiness")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_business", nullable = false)
    private Business idBusiness;

    @Column(name = "join_date", nullable = false)
    private Instant joinDate;

}