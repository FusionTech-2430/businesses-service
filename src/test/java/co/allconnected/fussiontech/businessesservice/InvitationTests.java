package co.allconnected.fussiontech.businessesservice;

import co.allconnected.fussiontech.businessesservice.controllers.BusinessController;
import co.allconnected.fussiontech.businessesservice.controllers.InvitationController;
import co.allconnected.fussiontech.businessesservice.dtos.*;
import co.allconnected.fussiontech.businessesservice.services.BusinessService;
import co.allconnected.fussiontech.businessesservice.services.InvitationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(InvitationController.class)
public class InvitationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvitationService invitationService;

    // Method to test Get all the business members for a business api/v1/businesses/{id_business}/members
    @Test
    void getBusinessMembersTest() throws Exception {
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

        when(invitationService.getBusinessMembers(businessId)).thenReturn(Arrays.asList(member1, member2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/businesses/" + businessId + "/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id.idUser").value("user1"))
                .andExpect(jsonPath("$[1].id.idUser").value("user2"));
    }

    // Get the last token for a business api/v1/businesses/{id_business}/join-token
    @Test
    void getLastInvitationTest() throws Exception {
        // Arrange
        UUID businessId = UUID.randomUUID();
        InvitationResponseDto invitation = new InvitationResponseDto(
                businessId,
                UUID.randomUUID(),
                new BusinessDto("Business 1", "owner1", businessId),
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(invitationService.getLastInvitation(businessId)).thenReturn(Collections.singletonList(invitation));

        // Act & Assert
        mockMvc.perform(get("/api/v1/businesses/" + businessId + "/join-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id_business").value(businessId.toString()))
                .andExpect(jsonPath("$[0].invitationToken").value(invitation.getInvitationToken().toString()));
    }

    // Delete a business member using a token api/v1/businesses/{id_business}/members/{id_user}
    @Test
    void deleteBusinessMemberTest() throws Exception {
        // Arrange
        UUID businessId = UUID.randomUUID();
        String userId = "user1";

        // Mock the service call
        doNothing().when(invitationService).deleteBusinessMember(businessId, userId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/businesses/" + businessId + "/members/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Business member deleted successfully"));
    }
}
