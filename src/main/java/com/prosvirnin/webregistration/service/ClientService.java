package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.user.Client;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.model.user.dto.EditStatus;
import com.prosvirnin.webregistration.repository.ClientRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final UserService userService;
    private final ClientRepository clientRepository;
    private final MasterRepository masterRepository;

    @Autowired
    public ClientService(UserService userService, ClientRepository clientRepository, MasterRepository masterRepository) {
        this.userService = userService;
        this.clientRepository = clientRepository;
        this.masterRepository = masterRepository;
    }

    @Transactional
    public void save(Client client){
        clientRepository.save(client);
    }


    @Transactional
    public EditResponse editMe(Authentication authentication, EditClientRequest editClientRequest){
        var user = (User) authentication.getPrincipal();
        var client = user.getClient();

        if (editClientRequest.getBirthDate() != null)
            client.setBirthDate(editClientRequest.getBirthDate());

        var response = userService.getEditResponse(editClientRequest, user);
        if (response.getStatus().equals(EditStatus.OK))
            clientRepository.save(client);
        return response;
    }

    @Transactional
    public void addMaster(Authentication authentication, Long masterId){
        var client = getAuthenticatedClient(authentication);
        var master = masterRepository.findById(masterId).orElseThrow();
        client.getMasters().add(master);
        master.getClients().add(client);
        clientRepository.save(client);
        masterRepository.save(master);
    }

    @Transactional
    public boolean deleteMaster(Authentication authentication, Long masterId){
        var client = getAuthenticatedClient(authentication);
        client.getMasters().remove(masterRepository.findById(masterId).orElseThrow());
        return true;
    }

    public Client getAuthenticatedClient(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return user.getClient();
    }
}
