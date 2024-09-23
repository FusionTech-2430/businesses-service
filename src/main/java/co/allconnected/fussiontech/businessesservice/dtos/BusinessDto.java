package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.Business}
 */
@Value
public class BusinessDto implements Serializable {
    String name;
    String owner_id;
    UUID organization;
}