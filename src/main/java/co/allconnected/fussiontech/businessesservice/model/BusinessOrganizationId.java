package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class BusinessOrganizationId implements Serializable {

    @Column(name = "id_organization", nullable = false)
    private UUID idOrganization;

    @Column(name = "id_business", nullable = false)
    private UUID idBusiness;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BusinessOrganizationId entity = (BusinessOrganizationId) o;
        return Objects.equals(this.idBusiness, entity.idBusiness) &&
                Objects.equals(this.idOrganization, entity.idOrganization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBusiness, idOrganization);
    }

}
