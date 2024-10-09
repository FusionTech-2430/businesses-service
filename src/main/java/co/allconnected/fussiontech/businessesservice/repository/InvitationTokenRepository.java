package co.allconnected.fussiontech.businessesservice.repository;

import co.allconnected.fussiontech.businessesservice.model.InvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, UUID> {

    Optional<InvitationToken> findByInvitationToken(UUID invitationToken);

    Collection<InvitationToken> findTop1ByIdBusinessIdOrderByCreationDateDesc(UUID idBusiness);
}