package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.service.dto.ServiceDTORequest;
import com.prosvirnin.webregistration.model.service.dto.ServiceDTOResponse;
import com.prosvirnin.webregistration.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/masters")
@Tag(
        name = "Service controller"
)
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Operation(
            description = "Adds a service to the master by token. Returns service id"
    )
    @PostMapping("/me/categories/{category_id}/services")
    private ResponseEntity<Long> createService(@PathVariable("category_id") Long id,
                                                 @RequestBody ServiceDTORequest serviceDTO,
                                                 Authentication authentication)
    {
        return ResponseEntity.ok(serviceService.save(id, serviceDTO, authentication));
    }

    @Operation(
            description = "Adds a service image to the master by token"
    )
    @PostMapping(value ="/me/services/{service_id}/edit/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> editServiceImage(@PathVariable("service_id") Long id,
                                                    @Parameter(description = "Upload a jpeg file") @RequestPart MultipartFile file,
                                                    Authentication authentication)
    {
        return ResponseEntity.ok(serviceService.editPicture(id, authentication, file));
    }

    @Operation(
            description = "Returns all services of the master by token"
    )
    @GetMapping(value ="/me/services")
    private ResponseEntity<List<ServiceDTOResponse>> getAllMyServices(Authentication authentication)
    {
        return ResponseEntity.ok(serviceService.findAllByAuthentication(authentication));
    }

    @Operation(
            description = "Returns all services of the master by master id"
    )
    @GetMapping(value ="/{id}/services")
    private ResponseEntity<List<ServiceDTOResponse>> getAllServicesByMasterId(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(serviceService.findAllByMasterId(id));
    }

    @Operation(
            description = "Download service image by service id"
    )
    @GetMapping("/services/{id}/image")
    public ResponseEntity<Resource> getProfilePicture (@PathVariable Long id)
    {
        Resource resource = serviceService.getPicture(id);
        if (resource == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @Operation(
            description = "Returns all services of this category"
    )
    @GetMapping(value ="/categories/{id}/services")
    private ResponseEntity<List<ServiceDTOResponse>> getAllServicesByCategoryId(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(serviceService.findAllByCategoryId(id));
    }

    @Operation(
            description = "Changing service"
    )
    @PutMapping("/me/services/{id}")
    public ResponseEntity<String> editMyService(Authentication authentication,
                                                   @PathVariable("id") Long id,
                                                   @RequestBody ServiceDTORequest edited){
        if (serviceService.edit(authentication, id, edited))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(
            description = "Removing a service"
    )
    @DeleteMapping("/me/services/{id}")
    public ResponseEntity<String> deleteMyService(Authentication authentication,
                                                     @PathVariable("id") Long id){
        if (serviceService.delete(authentication, id))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }
}
