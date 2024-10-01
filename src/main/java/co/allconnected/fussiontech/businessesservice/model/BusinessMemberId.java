package co.allconnected.fussiontech.businessesservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class BusinessMemberId implements Serializable {
    @Serial
    private static final long serialVersionUID = -6825435134008906622L;

    @Column(name = "id_user", nullable = false, length = 28)
    private String idUser;

    @Column(name = "id_business", nullable = false)
    private UUID idBusiness;

    public BusinessMemberId() {
    }
    public BusinessMemberId(String idUser, UUID idBusiness) {
        this.idUser = idUser;
        this.idBusiness = idBusiness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BusinessMemberId entity = (BusinessMemberId) o;
        return Objects.equals(this.idUser, entity.idUser) &&
                Objects.equals(this.idBusiness, entity.idBusiness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idBusiness);
    }

}