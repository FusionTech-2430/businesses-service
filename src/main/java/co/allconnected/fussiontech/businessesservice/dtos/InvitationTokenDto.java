package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.InvitationToken}
 */
@Getter
@Setter
@AllArgsConstructor
@Value
public class InvitationTokenDto implements Serializable {
    UUID invitationToken;
    BusinessDto idBusiness;
    Instant creationDate;
    Instant expirationDate;
}