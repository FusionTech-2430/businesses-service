package co.allconnected.fussiontech.businessesservice.services;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessDto;
import co.allconnected.fussiontech.businessesservice.dtos.BusinessMemberDto;
import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessMember;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganization;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganizationId;
import co.allconnected.fussiontech.businessesservice.repository.BusinessMemberRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessOrganizationRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessOrganizationRepository businessOrganizationRepository;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private BusinessMemberRepository businessMemberRepository;

    public BusinessResponseDto createBusiness(BusinessDto businessDto, MultipartFile logo) throws IOException {
        // Create a new Business entity from BusinessDto
        Business business = new Business();
        business.setId(UUID.randomUUID());
        business.setName(businessDto.getName());
        business.setOwnerId(businessDto.getOwner_id());

        // Handle the logo upload
        if (logo != null && !logo.isEmpty()) {
            String photoName = String.valueOf(business.getId());
            String extension = FilenameUtils.getExtension(logo.getOriginalFilename());
            try {
                business.setLogoUrl(firebaseService.uploadImg(photoName, extension, logo));
            } catch (IOException ignored) {
                //already error handling elsewhere
            }
        }
        // Save the business entity
        Business savedBusiness = businessRepository.save(business);

        // Create the BusinessOrganization relationship
        BusinessOrganizationId businessOrganizationId = new BusinessOrganizationId();
        businessOrganizationId.setIdBusiness(savedBusiness.getId());
        businessOrganizationId.setIdOrganization(businessDto.getOrganization());

        BusinessOrganization businessOrganization = new BusinessOrganization();
        businessOrganization.setId(businessOrganizationId);
        businessOrganization.setBusiness(savedBusiness);

        // Save the relationship
        businessOrganizationRepository.save(businessOrganization);

        return mapToResponseDto(savedBusiness);
    }
    public BusinessResponseDto getBusinessById(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + id));
        return mapToResponseDto(business);
    }

    public List<BusinessResponseDto> getAllBusinesses() {
        return businessRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public BusinessResponseDto mapToResponseDto(Business business) {
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

    public BusinessResponseDto updateBusiness(UUID id, BusinessDto businessDto, MultipartFile logo) {
        Business existingBusiness = businessRepository.findById(id)
                .orElseThrow(() -> new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado"));

        if (businessDto.getOwner_id() != null && !existingBusiness.getOwnerId().equals(businessDto.getOwner_id())) {
            throw new OperationException(HttpStatus.UNAUTHORIZED.value(), "No tienes permiso para actualizar este emprendimiento");
        }

        if (businessDto.getName() != null) {
            existingBusiness.setName(businessDto.getName());
        }

        if (logo != null && !logo.isEmpty()) {
            String photoName = String.valueOf(existingBusiness.getId());
            String extension = FilenameUtils.getExtension(logo.getOriginalFilename());
            try {
                existingBusiness.setLogoUrl(firebaseService.uploadImg(photoName, extension, logo));
            } catch (IOException e) {
                throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error subiendo la imagen");
            }
        }

        Business updatedBusiness = businessRepository.save(existingBusiness);
        return mapToResponseDto(updatedBusiness);
    }



    @Transactional
    public void deleteBusiness(UUID id) {
        Business existingBusiness = businessRepository.findById(id)
                .orElseThrow(() -> new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado"));

        businessOrganizationRepository.deleteByIdBusiness(existingBusiness.getId());
        businessRepository.delete(existingBusiness);
    }

    //businesses user is a part of
    public List<BusinessResponseDto> getBusinessesByPerson(String personId) {
        List<BusinessMember> businessMembers = businessMemberRepository.findById_IdUser(personId);
        return businessMembers.stream()
                .map(bm -> mapToResponseDto(bm.getIdBusiness()))
                .collect(Collectors.toList());
    }

    //businesses user is owner of
    public List<BusinessResponseDto> getBusinessesOwnedByPerson(String ownerId) {
        List<Business> businesses = businessRepository.findByOwnerId(ownerId);
        return businesses.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    //busineses user is associated to
    public List<BusinessResponseDto> getAllBusinessesForPerson(String personId) {
        List<BusinessResponseDto> businessesAsMember = getBusinessesByPerson(personId);
        List<BusinessResponseDto> businessesAsOwner = getBusinessesOwnedByPerson(personId);

        Set<BusinessResponseDto> allBusinesses = new HashSet<>(businessesAsMember);
        allBusinesses.addAll(businessesAsOwner);
        return new ArrayList<>(allBusinesses);
    }

}


