package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.BusinessMember;
import co.allconnected.fussiontech.businessesservice.model.BusinessMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessMemberRepository extends JpaRepository<BusinessMember, BusinessMemberId> {
}