package co.allconnected.fussiontech.businessesservice.controllers;

import co.allconnected.fussiontech.businessesservice.dtos.BusinessDto;
import co.allconnected.fussiontech.businessesservice.dtos.BusinessResponseDto;
import co.allconnected.fussiontech.businessesservice.exceptions.OperationException;
import co.allconnected.fussiontech.businessesservice.services.BusinessService;
import co.allconnected.fussiontech.businessesservice.utils.ErrorResponse;
import co.allconnected.fussiontech.businessesservice.utils.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping
    public ResponseEntity<?> createBusiness(
            @ModelAttribute BusinessDto businessDto,
            @RequestParam(value = "logo_url", required = false) MultipartFile logo) {
        try {
            BusinessResponseDto createdBusiness = businessService.createBusiness(businessDto, logo);
            return new ResponseEntity<>(createdBusiness, HttpStatus.CREATED);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error de Servidor");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBusinessById(@PathVariable UUID id) {
        try {
            BusinessResponseDto businessDto = businessService.getBusinessById(id);
            return new ResponseEntity<>(businessDto, HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (RuntimeException e) {
            throw new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBusinesses() {
        try {
            List<BusinessResponseDto> businesses = businessService.getAllBusinesses();
            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Emprendimientos no encontrados");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBusiness(
            @PathVariable UUID id,
            @ModelAttribute BusinessDto businessDto,
            @RequestParam(value = "logo_url", required = false) MultipartFile logo) {
        try {
            BusinessResponseDto updatedBusiness = businessService.updateBusiness(id, businessDto, logo);
            return new ResponseEntity<>(updatedBusiness, HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (RuntimeException e) {
            throw new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado para actualizar");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusiness(@PathVariable UUID id) {
        try {
            businessService.deleteBusiness(id);
            return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK.value(), "Emprendimiento eliminado correctamente"), HttpStatus.OK);
        } catch (OperationException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getCode(), e.getMessage()), HttpStatus.valueOf(e.getCode()));
        } catch (RuntimeException e) {
            throw new OperationException(HttpStatus.NOT_FOUND.value(), "Emprendimiento no encontrado para eliminar");
        }
    }

    //businesses user is a part of
    @GetMapping("/member/{personId}")
    public ResponseEntity<?> getBusinessesByPerson(@PathVariable String personId) {
        try {
            List<BusinessResponseDto> businesses = businessService.getBusinessesByPerson(personId);
            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching businesses for person");
        }
    }

    //businesses user is owner of
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getBusinessesOwnedByPerson(@PathVariable String ownerId) {
        try {
            List<BusinessResponseDto> businesses = businessService.getBusinessesOwnedByPerson(ownerId);
            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching businesses owned by person");
        }
    }

    //businesses user is associated with
    @GetMapping("/associated/{personId}")
    public ResponseEntity<?> getAllBusinessesForPerson(@PathVariable String personId) {
        try {
            List<BusinessResponseDto> businesses = businessService.getAllBusinessesForPerson(personId);
            return new ResponseEntity<>(businesses, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching all businesses for person");
        }
    }
}
