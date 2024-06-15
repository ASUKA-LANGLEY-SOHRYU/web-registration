package com.prosvirnin.webregistration.controller;

import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.ClientProfile;
import com.prosvirnin.webregistration.model.user.dto.MasterProfile;
import com.prosvirnin.webregistration.model.user.dto.UserDTO;
import com.prosvirnin.webregistration.service.MasterService;
import com.prosvirnin.webregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService {
    private final MasterService masterService;
    private final UserService userService;

    @Autowired
    public ProfileService(MasterService masterService, UserService userService) {
        this.masterService = masterService;
        this.userService = userService;
    }


    public UserDTO getAuthenticatedUserDTO(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return getUserDTOByUser(user);
    }

    public UserDTO getUserDTOByUserId(Long id){
        var user = userService.findById(id).orElseThrow();
        return getUserDTOByUser(user);
    }

    public UserDTO getUserDTOByUser(User user){
        MasterProfile master = null;
        ClientProfile client = null;
        if (user.getMaster() != null)
            master = masterService.getMasterProfile(user);
        else
            client = ClientProfile.fromClient(user.getClient());
        return  UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .emailToChange(user.getEmailToChange())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().toList())
                .master(master)
                .client(client)
                .build();
    }
}
