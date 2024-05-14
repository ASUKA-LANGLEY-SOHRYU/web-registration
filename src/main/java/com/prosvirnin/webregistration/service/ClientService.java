package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.repository.ClientRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final UserService userService;

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(UserService userService, ClientRepository clientRepository) {
        this.userService = userService;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public EditResponse editMe(Authentication authentication, EditClientRequest editClientRequest){
        var user = (User) authentication.getPrincipal();
        var client = user.getClient();

        if (editClientRequest.getBirthDate() != null)
            client.setBirthDate(editClientRequest.getBirthDate());

        var editUserDTO = editClientRequest.getEditUserDTO();
        try {
            userService.editUser(user, editUserDTO);
        } catch (EmailAlreadyExistsException e){
            return EditResponse.builder()
                    .status(e.getMessage())
                    .build();
        }
        userService.save(user);
        clientRepository.save(client);
        return EditResponse.builder()
                .status("OK!")
                .build();
    }
}