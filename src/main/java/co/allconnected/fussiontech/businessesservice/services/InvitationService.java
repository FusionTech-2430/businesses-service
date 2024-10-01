package co.allconnected.fussiontech.businessesservice.services;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessDto;
import co.allconnected.fussiontech.businessesservice.dtos.InvitationTokenDto;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.InvitationToken;
import co.allconnected.fussiontech.businessesservice.repository.BusinessRepository;
import co.allconnected.fussiontech.businessesservice.repository.InvitationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class InvitationService {

    private final InvitationTokenRepository invitationRepository;
    private final BusinessRepository businessRepository;

    public InvitationService(InvitationTokenRepository invitationRepository, BusinessRepository businessRepository) {
        this.invitationRepository = invitationRepository;
        this.businessRepository = businessRepository;
    }

    public String createInvitationToken(UUID idBusiness) {
        // Fetch the business entity from the database
        Business business = businessRepository.findById(idBusiness)
                .orElseThrow(() -> new RuntimeException("Business not found: " + idBusiness));

        // Generate a new invitation token
        String token = UUID.randomUUID().toString();
        Instant creationDate = Instant.now();
        Instant expirationDate = creationDate.plus(3, ChronoUnit.DAYS);

        // Create the InvitationToken object using setters
        InvitationToken invitationToken = new InvitationToken();
        invitationToken.setInvitationToken(token);
        invitationToken.setIdBusiness(business);
        invitationToken.setCreationDate(creationDate);
        invitationToken.setExpirationDate(expirationDate);

        // Save the invitation token in the repository
        invitationRepository.save(invitationToken);

        // Return the InvitationTokenDto (assuming BusinessDto has an appropriate constructor)
        return token;
    }
}
