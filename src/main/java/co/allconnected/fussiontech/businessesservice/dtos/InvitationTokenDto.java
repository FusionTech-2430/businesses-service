package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.InvitationToken}
 */
@Getter
@Setter
@AllArgsConstructor
@Value
public class InvitationTokenDto implements Serializable {
    String invitationToken;
    BusinessDto idBusiness;
    Instant creationDate;
    Instant expirationDate;
}