package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationResponseDto {
    private UUID id_business;
    private UUID invitationToken;
    private BusinessDto idBusiness;
    private Instant creationDate;
    private Instant expirationDate;
}
