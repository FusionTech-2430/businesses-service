package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.BusinessOrganization;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessOrganizationRepository extends JpaRepository<BusinessOrganization, BusinessOrganizationId> {
}