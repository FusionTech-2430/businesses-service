package co.allconnected.fussiontech.businessesservice.controllers;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessMemberDto;
import co.allconnected.fussiontech.businessesservice.dtos.InvitationResponseDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.services.InvitationService;
import co.allconnected.fussiontech.businessesservice.utils.ErrorResponse;
import co.allconnected.fussiontech.businessesservice.utils.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/businesses")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    // Get all the invitation tokens for a business (for debugging purposes)
    @GetMapping("/invitations")
    public ResponseEntity<?> getAllInvitations() {
        try {
            List<InvitationResponseDto> businesses = invitationService.getAllInvitations();
            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Emprendimientos no encontrados");
        }
    }

    // Create a new invitation token for a business
    @PostMapping("/{id_business}/join-token")
    public ResponseEntity<?> createInvitationToken(@PathVariable("id_business") UUID idBusiness) {
        try {
            UUID token = invitationService.createInvitationToken(idBusiness);
            return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK.value(),token.toString()), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error creating token: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add a business member using a token
    @PostMapping("/{id_business}/members")
    public ResponseEntity<?> addBusinessMemberUsingToken(@RequestBody Map<String, Object> requestBody, @PathVariable UUID id_business) {
        try {
            String userIdStr = (String) requestBody.get("id_user");
            String token = (String) requestBody.get("join_token");

            // Call the service with the token and userId (business is retrieved from the token)
            BusinessMemberDto businessMemberDto = invitationService.addBusinessMemberUsingToken(UUID.fromString(token), userIdStr, id_business);
            return new ResponseEntity<>(businessMemberDto, HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error adding user to business"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all the business members for a business
    @GetMapping("/{id_business}/members")
    public ResponseEntity<?> getBusinessMembers(@PathVariable UUID id_business) {
        try {
            List<BusinessMemberDto> businessMembers = invitationService.getBusinessMembers(id_business);
            return new ResponseEntity<>(businessMembers, HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error getting business members"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
