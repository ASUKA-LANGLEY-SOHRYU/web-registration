package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.*;
import com.prosvirnin.webregistration.repository.ImageRepository;
import com.prosvirnin.webregistration.repository.UserRepository;
import com.prosvirnin.webregistration.service.auth.AccountActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AccountActivationService accountActivationService;
    private final IFileService fileService;

    @Autowired
    public UserService(UserRepository userRepository, ImageRepository imageRepository, AccountActivationService accountActivationService, IFileService fileService) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.accountActivationService = accountActivationService;
        this.fileService = fileService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
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

        return EditResponse.ok();
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public EditResponse changeProfilePicture(Authentication authentication,
                                             MultipartFile multipartFile){
        try {
            Image image = fileService.saveImage(multipartFile);
            System.out.println(image.getType());
            var user = getAuthenticatedUser(authentication);
            user.setImage(image);
            imageRepository.save(image);
            userRepository.save(user);
        } catch (Exception e){
            return EditResponse.builder()
                    .status(EditStatus.ERROR)
                    .message(e.getMessage())
                    .build();
        }
        return EditResponse.ok();
    }

    public byte[] getProfilePicture(Authentication authentication){
        var user = getAuthenticatedUser(authentication);
        var fileName = user.getImage().getFileName();
        return fileService.getResource(fileName);
    }
}
