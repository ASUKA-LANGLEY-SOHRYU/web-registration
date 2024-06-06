package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@Tag(
        name = "Client controller"
)
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(
            description = "Editing a client profile. Values equal to null will be ignored. " +
                    "After changing your email, you will receive an activation on your new email." +
                    "After activation, you need to authenticate again."
    )
    @PostMapping("/me/edit")
    public ResponseEntity<EditResponse> me (Authentication authentication,
                                            @RequestBody EditClientRequest editClientRequest)
    {
        return ResponseEntity.ok(clientService.editMe(authentication, editClientRequest));
    }

    @Operation(description = "Removes a master from a client by token")
    @DeleteMapping("/me/masters/{id}")
    public ResponseEntity<String> removeMaster(Authentication authentication, @PathVariable("id") Long id){
        if (clientService.deleteMaster(authentication, id))
            return ResponseEntity.ok("OK!");
        return ResponseEntity.ok("ERROR!");
    }

    @Operation(description = "Adds a master from a client by token")
    @PutMapping("/me/masters/{id}")
    public ResponseEntity<String> addMaster(Authentication authentication, @PathVariable("id") Long id){
        clientService.addMaster(authentication, id);
        return ResponseEntity.ok("OK!");
    }
}
