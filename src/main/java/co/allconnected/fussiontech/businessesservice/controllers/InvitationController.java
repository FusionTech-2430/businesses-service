package co.allconnected.fussiontech.businessesservice.controllers;

import co.allconnected.fussiontech.businessesservice.dtos.InvitationTokenDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.services.InvitationService;
import co.allconnected.fussiontech.businessesservice.utils.ErrorResponse;
import co.allconnected.fussiontech.businessesservice.utils.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/business/{id_business}/join-token")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping()
    public ResponseEntity<?> createInvitationToken(@PathVariable("id_business") UUID idBusiness) {
        try {
            String token = invitationService.createInvitationToken(idBusiness);
            return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK.value(),token), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error creating token: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
