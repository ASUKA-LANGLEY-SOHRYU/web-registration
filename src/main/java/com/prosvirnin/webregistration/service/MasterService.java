package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.exception.auth.EmailAlreadyExistsException;
import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.model.service.dto.CategoryDTO;
import com.prosvirnin.webregistration.model.service.dto.ScheduleResponse;
import com.prosvirnin.webregistration.model.service.dto.ServiceDTOResponse;
import com.prosvirnin.webregistration.model.user.Address;
import com.prosvirnin.webregistration.model.user.Master;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.model.user.dto.EditMasterRequest;
import com.prosvirnin.webregistration.model.user.dto.EditResponse;
import com.prosvirnin.webregistration.model.user.dto.EditStatus;
import com.prosvirnin.webregistration.model.user.dto.MasterProfile;
import com.prosvirnin.webregistration.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MasterService {


    private final UserService userService;
    private final AddressRepository addressRepository;
    private final MasterRepository masterRepository;
    private final IFileService fileService;
    private final ImageRepository imageRepository;
    private final ServiceRepository serviceRepository;
    private final CategoryRepository categoryRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public MasterService(UserService userService, AddressRepository addressRepository, MasterRepository masterRepository, IFileService fileService, ImageRepository imageRepository, ServiceRepository serviceRepository, CategoryRepository categoryRepository, ScheduleRepository scheduleRepository) {
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.masterRepository = masterRepository;
        this.fileService = fileService;
        this.imageRepository = imageRepository;
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Master findById(Long id){
        return masterRepository.findById(id).orElseThrow();
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

        var response = userService.getEditResponse(editMasterRequest, user);
        if (response.getStatus().equals(EditStatus.OK))
            masterRepository.save(master);
        return response;
    }
    @Transactional
    public void save(Master master){
        masterRepository.save(master);
    }


    @Transactional
    public boolean uploadAdditionalImage(Authentication authentication, MultipartFile file){
        var master = getAuthenticatedMaster(authentication);
        Image image;
        try {
            image = fileService.saveImage(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (master.getAdditionalImages() == null)
            master.setAdditionalImages(new ArrayList<>());
        master.getAdditionalImages().add(image);
        image.setMaster(master);
        imageRepository.save(image);
        masterRepository.save(master);
        return true;
    }


    public List<Long> getAllAdditionalImagesByAuthenticatedMaster(Authentication authentication){
        var master = getAuthenticatedMaster(authentication);
        return getAllAdditionalImagesByMaster(master);
    }

    public List<Long> getAllAdditionalImagesByMasterId(Long id){
        var master = masterRepository.findById(id).orElseThrow();
        return getAllAdditionalImagesByMaster(master);
    }

    private List<Long> getAllAdditionalImagesByMaster(Master master){
        return master.getAdditionalImages()
                .stream()
                .map(Image::getId)
                .toList();
    }

    @Transactional
    public boolean deleteAdditionalImageById(Authentication authentication, Long imageId){
        var master = getAuthenticatedMaster(authentication);
        var image = imageRepository.findById(imageId).orElseThrow();
        if(!image.getMaster().getId().equals(master.getId()))
            return false;
        var result =  fileService.deleteFileByImage(image);
        imageRepository.deleteById(imageId);
        return result;
    }

    public MasterProfile getMasterProfile(User masterAsUser){
        var image = masterAsUser.getImage();
        Long imgid = null;
        if (image != null)
            imgid = image.getId();

        return MasterProfile.builder()
                .masterId(masterAsUser.getMaster().getId())
                .fullName(masterAsUser.getFirstName() + " " + masterAsUser.getLastName())
                .description(masterAsUser.getMaster().getDescription())
                .address(masterAsUser.getMaster().getAddress())
                .messenger(masterAsUser.getMaster().getMessenger())
                .profilePictureId(imgid)
                .additionalImagesIds(masterAsUser.getMaster().getAdditionalImages().stream().map(Image::getId).toList())
                .phoneNumber(masterAsUser.getPhone())
                .services(serviceRepository.findByMasterId(masterAsUser.getMaster().getId()).stream().map(ServiceDTOResponse::fromService).toList())
                .categories(categoryRepository.findByMasterId(masterAsUser.getMaster().getId()))
                .schedule(ScheduleResponse.fromSchedules(scheduleRepository.findAllByMasterId(masterAsUser.getMaster().getId())))
                .build();
    }

    public MasterProfile getMasterProfile(Long userId){
        var masterAsUser = userService.findById(userId).orElseThrow();
        return getMasterProfile(masterAsUser);
    }

    public Master getAuthenticatedMaster(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return masterRepository.findById(user.getMaster().getId()).orElseThrow();
    }

}
