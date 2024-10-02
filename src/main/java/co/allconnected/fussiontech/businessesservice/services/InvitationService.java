package co.allconnected.fussiontech.businessesservice.services;

import co.allconnected.fussiontech.businessesservice.dtos.*;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessMember;
import co.allconnected.fussiontech.businessesservice.model.BusinessMemberId;
import co.allconnected.fussiontech.businessesservice.model.InvitationToken;
import co.allconnected.fussiontech.businessesservice.repository.BusinessMemberRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessRepository;
import co.allconnected.fussiontech.businessesservice.repository.InvitationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final InvitationTokenRepository invitationRepository;
    private final BusinessRepository businessRepository;
    private final BusinessMemberRepository businessMemberRepository;

    public InvitationService(InvitationTokenRepository invitationRepository, BusinessRepository businessRepository, BusinessMemberRepository businessMemberRepository) {
        this.invitationRepository = invitationRepository;
        this.businessRepository = businessRepository;
        this.businessMemberRepository = businessMemberRepository;
    }

    @Autowired
    private InvitationTokenRepository invitationTokenRepository;

    public UUID createInvitationToken(UUID idBusiness) {
        // Fetch the business entity from the database
        Business business = businessRepository.findById(idBusiness)
                .orElseThrow(() -> new RuntimeException("Business not found: " + idBusiness));

        // Generate a new invitation token
        UUID token = UUID.randomUUID();
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

    @Transactional
    public BusinessMemberDto addBusinessMemberUsingToken(UUID token, String userId, UUID idBusiness) {
        // Use the repository to find the token
        InvitationToken invitationToken = invitationTokenRepository.findByInvitationToken(token)
                .orElseThrow(() -> new OperationException(HttpStatus.BAD_REQUEST.value(), "Invalid or expired token"));

        // Check if the token has expired
        if (invitationToken.getExpirationDate().isBefore(Instant.now())) {
            throw new OperationException(HttpStatus.BAD_REQUEST.value(), "Token has expired");
        }

        // Fetch the business to which the user is being added
        Business business = businessRepository.findById(idBusiness)
                .orElseThrow(() -> new OperationException(HttpStatus.NOT_FOUND.value(), "Business not found"));

        // Check if the user is already a member
        BusinessMemberId businessMemberId = new BusinessMemberId(userId, business.getId());
        if (businessMemberRepository.existsById(businessMemberId)) {
            throw new OperationException(HttpStatus.CONFLICT.value(), "User is already a member of the business");
        }

        // Add the user to the business as a member
        BusinessMember businessMember = new BusinessMember();
        businessMember.setId(businessMemberId);
        businessMember.setIdBusiness(business);
        businessMember.setJoinDate(Instant.now());

        businessMemberRepository.save(businessMember);

        invitationTokenRepository.delete(invitationToken);

        // Return the response as DTO
        return new BusinessMemberDto(
                new BusinessMemberIdDto(userId, business.getId()),
                new BusinessDto(business.getName(), business.getOwnerId(), business.getId()),
                businessMember.getJoinDate()
        );
    }

    // Get all the business members for a business
    public List<BusinessMemberDto> getBusinessMembers(UUID idBusiness) {
        Business business = businessRepository.findById(idBusiness)
                .orElseThrow(() -> new OperationException(HttpStatus.NOT_FOUND.value(), "Business not found"));

        return businessMemberRepository.findByIdBusiness(business)
                .stream()
                .map(this::mapToBusinessMemberDto)
                .collect(Collectors.toList());
    }

    public BusinessMemberDto mapToBusinessMemberDto(BusinessMember businessMember) {
        return new BusinessMemberDto(
                new BusinessMemberIdDto(businessMember.getId().getIdUser(), businessMember.getId().getIdBusiness()),
                new BusinessDto(businessMember.getIdBusiness().getName(), businessMember.getIdBusiness().getOwnerId(), businessMember.getIdBusiness().getId()),
                businessMember.getJoinDate()
        );
    }

    // Get all the invitation tokens for a business (for debugging purposes)
    public List<InvitationResponseDto> getAllInvitations() {
        return invitationRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public InvitationResponseDto mapToResponseDto(InvitationToken invitationToken) {
    return new InvitationResponseDto(
            invitationToken.getIdBusiness().getId(),
            invitationToken.getInvitationToken(),
            new BusinessDto(
                invitationToken.getIdBusiness().getName(),
                invitationToken.getIdBusiness().getOwnerId(),
                invitationToken.getIdBusiness().getId()
            ),
            invitationToken.getCreationDate(),
            invitationToken.getExpirationDate()
        );
    }
}
