package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "invitation_token", schema = "all_connected_businesses")
public class InvitationToken {
    @Id
    @SequenceGenerator(name = "invitation_token_id_gen", sequenceName = "conveyance_request_id_conveyance_request_seq", allocationSize = 1)
    @Column(name = "invitation_token", nullable = false, length = 28)
    private String invitationToken;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_business", nullable = false)
    private Business idBusiness;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

}