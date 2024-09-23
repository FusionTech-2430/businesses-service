package co.allconnected.fussiontech.businessesservice.services;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessDto;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganization;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganizationId;
import co.allconnected.fussiontech.businessesservice.repository.BusinessRepository;
import co.allconnected.fussiontech.businessesservice.repository.BusinessOrganizationRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessOrganizationRepository businessOrganizationRepository;

    @Autowired
    private FirebaseService firebaseService;

    public Business createBusiness(BusinessDto businessDto, MultipartFile logo) throws IOException {
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
            } catch (IOException e) {
                //todo
                e.printStackTrace();
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
        businessOrganization.setIdBusiness(savedBusiness);

        // Save the relationship
        businessOrganizationRepository.save(businessOrganization);

        return savedBusiness;
    }
}
