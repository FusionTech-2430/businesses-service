package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganization;
import co.allconnected.fussiontech.businessesservice.model.BusinessOrganizationId;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BusinessOrganizationRepository extends JpaRepository<BusinessOrganization, BusinessOrganizationId> {
    List<BusinessOrganization> findById_IdBusiness(UUID idBusiness);

    @Modifying
    @Query("DELETE FROM BusinessOrganization bo WHERE bo.id.idBusiness = :idBusiness")
    void deleteByIdBusiness(@Param("idBusiness") UUID idBusiness);
}