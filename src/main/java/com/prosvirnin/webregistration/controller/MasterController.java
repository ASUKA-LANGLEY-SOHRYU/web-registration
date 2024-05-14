package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditMasterRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.service.MasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/masters")
@Tag(
        name = "Master controller"
)
public class MasterController {

    private final MasterService masterService;

    @Autowired
    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @Operation(
            description = "Editing a master profile. Values equal to null will be ignored. " +
                    "After changing your email, you will receive an activation on your new email." +
                    "After activation, you need to authenticate again."
    )
    @PostMapping("/me/edit")
    public ResponseEntity<EditResponse> me (Authentication authentication,
                                            @RequestBody EditMasterRequest editMasterRequest)
    {
        return ResponseEntity.ok(masterService.editMe(authentication, editMasterRequest));
    }
}
