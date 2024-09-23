package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.InvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, String> {
}