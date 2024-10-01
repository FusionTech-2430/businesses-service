package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.BusinessMember}
 */
@Value
public class BusinessMemberDto implements Serializable {
    BusinessMemberIdDto id;
    BusinessDto idBusiness;
    Instant joinDate;
}