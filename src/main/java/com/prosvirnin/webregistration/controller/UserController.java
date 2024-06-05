package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.model.user.dto.UserDTO;
import com.prosvirnin.webregistration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "User controller"
)
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;

    @Autowired
    public UserController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @Operation(
            description = "Getting a user by token"
    )
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me (Authentication authentication)
    {
        return ResponseEntity.ok(profileService.getAuthenticatedUserDTO(authentication));
    }

    @Operation(
            description = "Getting a user by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getById (@PathVariable("id") Long id)
    {
        return ResponseEntity.ok(userService.findById(id).orElse(null));
    }



    @Operation(
            description = "Upload profile image"
    )
    @PostMapping(value = "/me/edit/profilePicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EditResponse> changeProfilePicture (
            Authentication authentication,
            @Parameter(description = "Upload a jpeg file") @RequestPart MultipartFile file)
    {
        return ResponseEntity.ok(userService.changeProfilePicture(authentication, file));
    }

    @Operation(
            description = "Download profile picture"
    )
    @GetMapping("/me/get/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture (Authentication authentication)
    {
        byte[] resource = userService.getProfilePicture(authentication);
        if (resource == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
