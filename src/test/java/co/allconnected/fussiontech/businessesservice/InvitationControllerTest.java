package co.allconnected.fussiontech.businessesservice;

import co.allconnected.fussiontech.businessesservice.controllers.InvitationController;
import co.allconnected.fussiontech.businessesservice.dtos.*;
import co.allconnected.fussiontech.businessesservice.services.InvitationService;
import co.allconnected.fussiontech.businessesservice.utils.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class InvitationControllerTest {

    @InjectMocks
    private InvitationController invitationController;

    @Mock
    private InvitationService invitationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBusinessMembersTest() {
        // Arrange
        UUID businessId = UUID.randomUUID();
        BusinessMemberDto member1 = new BusinessMemberDto(
                new BusinessMemberIdDto("user1", businessId),
                new BusinessDto("Business 1", "owner1", businessId),
                Instant.now()
        );
        BusinessMemberDto member2 = new BusinessMemberDto(
                new BusinessMemberIdDto("user2", businessId),
                new BusinessDto("Business 1", "owner1", businessId),
                Instant.now()
        );

        when(invitationService.getBusinessMembers(businessId))
                .thenReturn(Arrays.asList(member1, member2));

        // Act
        ResponseEntity<?> response = invitationController.getBusinessMembers(businessId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<BusinessMemberDto> actualMembers = (List<BusinessMemberDto>) response.getBody();
        assertNotNull(actualMembers);
        assertEquals(2, actualMembers.size());
        assertEquals("user1", actualMembers.get(0).getId().getIdUser());
        assertEquals("user2", actualMembers.get(1).getId().getIdUser());
    }

    @Test
    void getLastInvitationTest() {
        // Arrange
        UUID businessId = UUID.randomUUID();
        UUID invitationToken = UUID.randomUUID();
        InvitationResponseDto invitation = new InvitationResponseDto(
                businessId,
                invitationToken,
                new BusinessDto("Business 1", "owner1", businessId),
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(invitationService.getLastInvitation(businessId))
                .thenReturn(Collections.singletonList(invitation));

        // Act
        ResponseEntity<?> response = invitationController.getLastInvitation(businessId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<InvitationResponseDto> actualInvitations = (List<InvitationResponseDto>) response.getBody();
        assertNotNull(actualInvitations);
        assertEquals(1, actualInvitations.size());
        assertEquals(businessId, actualInvitations.get(0).getId_business());
        assertEquals(invitationToken, actualInvitations.get(0).getInvitationToken());
    }

    @Test
    void deleteBusinessMemberTest() {
        // Arrange
        UUID businessId = UUID.randomUUID();
        String userId = "user1";

        doNothing().when(invitationService).deleteBusinessMember(businessId, userId);

        // Act
        ResponseEntity<?> response = invitationController.deleteBusinessMember(businessId, userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof SuccessResponse);

        SuccessResponse successResponse = (SuccessResponse) response.getBody();
        assertEquals(HttpStatus.OK.value(), successResponse.getCode());
        assertEquals("Business member deleted successfully", successResponse.getMessage());
    }
}