package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.BusinessMemberId}
 */
@Value
public class BusinessMemberIdDto implements Serializable {
    String idUser;
    UUID idBusiness;
}