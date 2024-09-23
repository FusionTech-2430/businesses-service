package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link co.allconnected.fussiontech.businessesservice.model.ConveyanceRequest}
 */
@Value
public class ConveyanceRequestDto implements Serializable {
    Integer id;
    Boolean accepted;
    Boolean active;
    Instant requestDate;
}