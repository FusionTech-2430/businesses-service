package co.allconnected.fussiontech.businessesservice.services;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessMember;
import co.allconnected.fussiontech.businessesservice.model.BusinessMemberId;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganization;
import co.allconnected.fussiontech.businessesservice.repository.BusinessMemberRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessOrganizationRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessMemberRepository businessMemberRepository;
    @Autowired
    private BusinessOrganizationRepository businessOrganizationRepository;

    @Transactional
    public BusinessResponseDto transferOwnership(UUID idBusiness, String ownerId, String userId) {
        // Fetch the business and validate the owner
        Business business = businessRepository.findById(idBusiness)
                .orElseThrow(() -> new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado"));

        if (!business.getOwnerId().equals(ownerId)) {
            throw new OperationException(HttpStatus.UNAUTHORIZED.value(), "No Autorizado");
        }

        // Check if user is a member of the business
        BusinessMemberId businessMemberId = new BusinessMemberId(userId, idBusiness);
        if (!businessMemberRepository.existsById(businessMemberId)) {
            throw new OperationException(HttpStatus.BAD_REQUEST.value(), "Usuario no hace parte del emprendimiento");
        }

        // Add the current owner as a member of the business
        BusinessMemberId currentOwnerMemberId = new BusinessMemberId(ownerId, idBusiness);
        if (!businessMemberRepository.existsById(currentOwnerMemberId)) {
            BusinessMember currentOwnerMember = new BusinessMember();
            currentOwnerMember.setId(currentOwnerMemberId);
            currentOwnerMember.setIdBusiness(business);
            currentOwnerMember.setJoinDate(Instant.now());
            businessMemberRepository.save(currentOwnerMember);
        }

        // Remove user from the business members
        businessMemberRepository.deleteById(businessMemberId);

        // Transfer the ownership of the business to the user
        business.setOwnerId(userId);
        businessRepository.save(business);

        // Building the response
        List<UUID> organizationIds = businessOrganizationRepository.findById_IdBusiness(business.getId())
                .stream()
                .map(org -> org.getId().getIdOrganization())
                .collect(Collectors.toList());

        return new BusinessResponseDto(
                business.getId(),
                business.getName(),
                organizationIds,
                business.getOwnerId(),
                business.getLogoUrl()
        );
    }
}
