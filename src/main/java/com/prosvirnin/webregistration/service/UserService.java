package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.model.user.dto.EditStatus;
import com.prosvirnin.webregistration.model.user.dto.EditUserDTO;
import com.prosvirnin.webregistration.model.user.dto.EditUserRequest;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.auth.AccountActivationService;
import com.prosvirnin.webregistration.service.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AccountActivationService accountActivationService;

    @Autowired
    public UserService(UserRepository userRepository, AccountActivationService accountActivationService) {
        this.userRepository = userRepository;
        this.accountActivationService = accountActivationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getAuthenticatedUser(Authentication authentication){
        return (User) authentication.getPrincipal();
    }

    public void editUser(User user, EditUserDTO editUserDTO){
        if (editUserDTO.getEmail() != null) {
            if (userRepository.existsByEmail(editUserDTO.getEmail()) ||
                    userRepository.existsByEmailToChange(editUserDTO.getEmail()))
                throw new EmailAlreadyExistsException(String.format(
                        "Email %s already exist.", editUserDTO.getEmail()));
            accountActivationService.sendActivationCode(user);
            user.setEmailToChange(editUserDTO.getEmail());
        }
        if (editUserDTO.getFirstName() != null)
            user.setFirstName(editUserDTO.getFirstName());
        if (editUserDTO.getLastName() != null)
            user.setLastName(editUserDTO.getLastName());
        if (editUserDTO.getPhoneNumber() != null)
            user.setPhone(editUserDTO.getPhoneNumber());
    }

    @Transactional
    public EditResponse getEditResponse(EditUserRequest editUserRequest, User user){
        var editUserDTO = editUserRequest.getUser();
        try {
            editUser(user, editUserDTO);
        } catch (EmailAlreadyExistsException e){
            return EditResponse.builder()
                    .message(e.getMessage())
                    .status(EditStatus.ERROR)
                    .build();
        }
        save(user);

        return EditResponse.builder()
                .status(EditStatus.OK)
                .build();
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }
}
