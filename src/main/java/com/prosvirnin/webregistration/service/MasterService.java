package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.user.Address;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditClientRequest;
import com.prosvirnin.webregistration.model.user.dto.EditMasterRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.repository.AddressRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MasterService {


    private final UserService userService;
    private final AddressRepository addressRepository;
    private final MasterRepository masterRepository;

    @Autowired
    public MasterService(UserService userService, AddressRepository addressRepository, MasterRepository masterRepository) {
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.masterRepository = masterRepository;
    }

    @Transactional
    public EditResponse editMe(Authentication authentication, EditMasterRequest editMasterRequest){
        var user = (User) authentication.getPrincipal();
        var master = masterRepository.findById(user.getMaster().getId()).orElseThrow();


        if (editMasterRequest.getDescription() != null)
            master.setDescription(editMasterRequest.getDescription());
        if (editMasterRequest.getAddress() != null) {
            Address address;
            if (master.getAddress() == null) {
                address = editMasterRequest.getAddress().map();
                address.setMaster(master);
                master.setAddress(address);
            } else {
                address = master.getAddress();
                address.setFields(editMasterRequest.getAddress().map());
            }
            addressRepository.save(address);
        }
        if (editMasterRequest.getLinkCode() != null)
            master.setLinkCode(editMasterRequest.getLinkCode());

        var editUserDTO = editMasterRequest.getEditUserDTO();
        try {
            userService.editUser(user, editUserDTO);
        } catch (EmailAlreadyExistsException e){
            return EditResponse.builder()
                    .status(e.getMessage())
                    .build();
        }

        userService.save(user);
        masterRepository.save(master);
        return EditResponse.builder()
                .status("OK!")
                .build();
    }
}
