package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.dto.EditMasterRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.model.user.dto.MasterProfile;
import com.prosvirnin.webregistration.service.IFileService;
import com.prosvirnin.webregistration.service.MasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/masters")
@Tag(
        name = "Master controller"
)
public class MasterController {

    private final MasterService masterService;
    private final IFileService fileService;

    @Autowired
    public MasterController(MasterService masterService, IFileService fileService) {
        this.masterService = masterService;
        this.fileService = fileService;
    }

    @Operation(
            description = "Editing a master profile. Values equal to null will be ignored. " +
                    "After changing your email, you will receive an activation on your new email. " +
                    "After activation, you need to authenticate again."
    )
    @PostMapping("/me/edit")
    public ResponseEntity<EditResponse> me (Authentication authentication,
                                            @RequestBody EditMasterRequest editMasterRequest)
    {
        return ResponseEntity.ok(masterService.editMe(authentication, editMasterRequest));
    }

    @Operation(description = "Uploads an additional image.")
    @PostMapping(value = "/me/upload_image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAdditionalImage(Authentication authentication,
                                                        @Parameter(description = "Upload a jpeg file")
                                                        @RequestPart MultipartFile file){
        if (masterService.uploadAdditionalImage(authentication, file))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(description = "Returns all additional image ids by token")
    @GetMapping("/me/additional_images")
    public ResponseEntity<List<Long>> getAllMyAdditionalImages(Authentication authentication){
        return ResponseEntity.ok(masterService.getAllAdditionalImagesByAuthenticatedMaster(authentication));
    }

    @Operation(description = "Returns all additional image ids by master id")
    @GetMapping("/{id}/additional_images")
    public ResponseEntity<List<Long>> getAllMyAdditionalImages(@PathVariable("id") Long id){
        return ResponseEntity.ok(masterService.getAllAdditionalImagesByMasterId(id));
    }

//    @Operation(description = "Returns additional image by image id")
//    @GetMapping("/additional_images/{id}")
//    public ResponseEntity<byte[]> getAdditionalImageById(@PathVariable("id") Long id){
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(fileService.getImageById(id));
//    }

    @Operation(description = "Deletes an image by id")
    @DeleteMapping("/additional_images/{id}")
    public ResponseEntity<String> deleteAdditionalImageById(Authentication authentication,
                                                            @PathVariable("id") Long id){
        if (masterService.deleteAdditionalImageById(authentication, id))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(description = "Returns the master by id.")
    @GetMapping("/{id}")
    public ResponseEntity<MasterProfile> getMasterById(@PathVariable("id") Long id){
        return ResponseEntity.ok(masterService.getMasterProfile(id));
    }
}
