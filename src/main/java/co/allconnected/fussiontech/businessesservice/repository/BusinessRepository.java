package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
  List<Business> findByOwnerId(String ownerId);
}