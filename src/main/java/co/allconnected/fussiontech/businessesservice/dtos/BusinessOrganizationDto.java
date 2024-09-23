package co.allconnected.fussiontech.businessesservice.dtos;

import co.allconnected.fussiontech.businessesservice.model.BusinessOrganizationId;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.BusinessOrganization}
 */
@Value
public class BusinessOrganizationDto implements Serializable {
    BusinessOrganizationId id;
    BusinessDto idBusiness;
}