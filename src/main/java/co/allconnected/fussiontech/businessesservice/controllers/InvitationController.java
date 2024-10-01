package co.allconnected.fussiontech.businessesservice.controllers;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessMemberDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.services.InvitationService;
import co.allconnected.fussiontech.businessesservice.utils.ErrorResponse;
import co.allconnected.fussiontech.businessesservice.utils.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/business/{id_business}")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/join-token")
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

    @PostMapping("/add-member")
    public ResponseEntity<?> addBusinessMemberUsingToken(@RequestBody Map<String, Object> requestBody) {
        try {
            String token = (String) requestBody.get("token");
            String userIdStr = (String) requestBody.get("userId");

            // Call the service with the token and userId (business is retrieved from the token)
            BusinessMemberDto businessMemberDto = invitationService.addBusinessMemberUsingToken(UUID.fromString(token), userIdStr);

            return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK.value(), "User successfully added to business"), HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error adding user to business"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
