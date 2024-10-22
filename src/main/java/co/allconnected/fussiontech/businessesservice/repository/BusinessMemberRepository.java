package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.model.BusinessMember;
import co.allconnected.fussiontech.businessesservice.model.BusinessMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, BusinessMemberId> {
    Collection<BusinessMember> findByIdBusiness(Business idBusiness);
    List<BusinessMember> findById_IdUser(String idUser);
}
