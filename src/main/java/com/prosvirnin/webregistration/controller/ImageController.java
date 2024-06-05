package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/images")
public class ImageController {

    private final IFileService fileService;

    @Autowired
    public ImageController(IFileService fileService) {
        this.fileService = fileService;
    }

    @Operation(description = "Returns an image by id")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") Long id){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileService.getImageById(id));
    }
}
