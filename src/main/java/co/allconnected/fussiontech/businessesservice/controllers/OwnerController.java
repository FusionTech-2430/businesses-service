package co.allconnected.fussiontech.businessesservice.controllers;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.services.OwnerService;
import co.allconnected.fussiontech.businessesservice.utils.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/{id_business}/members/{id_user}/owner-requests")
    public ResponseEntity<?> transferOwnership(@PathVariable("id_business") UUID idBusiness,
                                               @PathVariable("id_user") String userId,
                                               @RequestBody Map<String, Object> requestBody) {
        try {
            String ownerId = (String) requestBody.get("owner_id");
            BusinessResponseDto businessResponseDto = ownerService.transferOwnership(idBusiness, ownerId, userId);
            return new ResponseEntity<>(businessResponseDto, HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error processing the request"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
