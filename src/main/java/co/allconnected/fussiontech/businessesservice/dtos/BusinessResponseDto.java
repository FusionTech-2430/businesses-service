package co.allconnected.fussiontech.businessesservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponseDto {
    private UUID id_business;
    private String name;
    private List<UUID> organizations;
    private String owner_id;
    private String logo_url;
}