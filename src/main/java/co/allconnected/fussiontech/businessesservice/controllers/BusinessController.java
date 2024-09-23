package co.allconnected.fussiontech.businessesservice.controllers;


import co.allconnected.fussiontech.businessesservice.dtos.BusinessDto;
import co.allconnected.fussiontech.businessesservice.model.Business;
import co.allconnected.fussiontech.businessesservice.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping
    public ResponseEntity<?> createBusiness(
            @ModelAttribute BusinessDto businessDto,
            @RequestParam(value = "logo_url", required = false) MultipartFile logo
    ) {
        try {
            Business createdBusiness = businessService.createBusiness(businessDto, logo);
            return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}