package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.Image;
import com.prosvirnin.webregistration.model.service.ServiceStatus;
import com.prosvirnin.webregistration.model.service.dto.ServiceDTORequest;
import com.prosvirnin.webregistration.model.service.dto.ServiceDTOResponse;
import com.prosvirnin.webregistration.repository.ImageRepository;
import com.prosvirnin.webregistration.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final IFileService fileService;
    private final CategoryService categoryService;
    private final ImageRepository imageRepository;
    private final MasterService masterService;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, @Qualifier("s3Service") IFileService fileService, CategoryService categoryService, ImageRepository imageRepository, MasterService masterService) {
        this.serviceRepository = serviceRepository;
        this.fileService = fileService;
        this.categoryService = categoryService;
        this.imageRepository = imageRepository;
        this.masterService = masterService;
    }

    @Transactional
    public Long save(Long categoryId, ServiceDTORequest serviceDTO, Authentication authentication){
        var master = masterService.getAuthenticatedMaster(authentication);
        var service = com.prosvirnin.webregistration.model.service.Service.builder()
                .master(master)
                .name(serviceDTO.getName())
                .category(categoryService.findById(categoryId))
                .description(serviceDTO.getDescription())
                .price(serviceDTO.getPrice())
                .duration(serviceDTO.getDBDuration())
                .serviceStatus(ServiceStatus.STATUS1) //TODO: Добавить статусы
                .build();
        System.out.println(service.getDuration());
        return serviceRepository.save(service).getId();
    }

    @Transactional
    public String editPicture(Long serviceId, Authentication authentication,
                              MultipartFile serviceImage){
        var master = masterService.getAuthenticatedMaster(authentication);
        if(master == null)
            return "ERROR!";
        Image image;
        try{
            image = fileService.saveImage(serviceImage);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "ERROR!";
        }
        var service = serviceRepository.findById(serviceId).orElseThrow();
        service.setImage(image);
        imageRepository.save(image);
        serviceRepository.save(service);
        return "OK!";
    }

    public byte[] getPicture(Long serviceId){
        var image = serviceRepository.findById(serviceId).orElseThrow()
                .getImage();
        if (image == null)
            return null;
        return fileService.getResource(image.getFileName());
    }

    public List<ServiceDTOResponse> findAllByAuthentication(Authentication authentication){
        return findAllByMasterId(masterService.getAuthenticatedMaster(authentication).getId());
    }

    public List<ServiceDTOResponse> findAllByMasterId(Long id){
        return serviceRepository.findByMasterId(id)
                .stream()
                .map(ServiceDTOResponse::fromService)
                .collect(Collectors.toList());
    }

    public List<ServiceDTOResponse> findAllByCategoryId(Long id){
        return serviceRepository.findByCategoryId(id)
                .stream()
                .map(ServiceDTOResponse::fromService)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean edit(Authentication authentication, Long serviceId, ServiceDTORequest serviceDTO){
        var service = serviceRepository.findById(serviceId).orElseThrow();
        if (!masterService.getAuthenticatedMaster(authentication).getId().equals(service.getMaster().getId())){
            return false;
        }
        editService(service, serviceDTO);
        serviceRepository.save(service);
        return true;
    }

    @Transactional
    void editService(com.prosvirnin.webregistration.model.service.Service source, ServiceDTORequest edited){
        if(edited.getName() != null)
            source.setName(edited.getName());
        if(edited.getDescription() != null)
            source.setDescription(edited.getDescription());
        if(edited.getPrice() != null)
            source.setPrice(edited.getPrice());
        if(edited.getDuration() != null)
            source.setDuration(edited.getDBDuration());
    }

    @Transactional
    public boolean delete(Authentication authentication, Long serviceId){
        var service = serviceRepository.findById(serviceId).orElseThrow();
        if (!masterService.getAuthenticatedMaster(authentication).getId().equals(service.getMaster().getId())){
            return false;
        }
        serviceRepository.deleteById(serviceId);
        return true;
    }
}
