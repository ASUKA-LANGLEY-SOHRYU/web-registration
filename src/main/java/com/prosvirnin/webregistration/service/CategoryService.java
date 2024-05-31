package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.Category;
import com.prosvirnin.webregistration.model.service.dto.CategoryDTO;
import com.prosvirnin.webregistration.model.user.Master;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.repository.CategoryRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import com.prosvirnin.webregistration.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ServiceRepository serviceRepository;
    private final MasterService masterService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ServiceRepository serviceRepository, MasterService masterService) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
        this.masterService = masterService;
    }

    @Transactional
    public Long save(Authentication authentication, Category category){
        category.setMaster(masterService.getAuthenticatedMaster(authentication));
        return categoryRepository.save(category).getId();
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional
    public boolean edit(Authentication authentication, Long categoryId, CategoryDTO edited){
        var category = categoryRepository.findById(categoryId).orElseThrow();
        if (!masterService.getAuthenticatedMaster(authentication).getId().equals(category.getMaster().getId())){
            return false;
        }
        editCategory(category, edited);
        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public boolean delete(Authentication authentication, Long categoryId){
        var category = categoryRepository.findById(categoryId).orElseThrow();
        if (!masterService.getAuthenticatedMaster(authentication).getId().equals(category.getMaster().getId())){
            return false;
        }
        serviceRepository.deleteByCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
        return true;
    }

    @Transactional
    void edit(Long categoryId, CategoryDTO edited){
        var category = categoryRepository.findById(categoryId).orElseThrow();

        editCategory(category, edited);

        categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long categoryId){
        categoryRepository.deleteById(categoryId);
    }

    private void editCategory(Category source, CategoryDTO edited){
        if (edited.getName() != null)
            source.setName(edited.getName());
        if (edited.getDescription() != null)
            source.setDescription(edited.getDescription());
    }

    public List<Category> findAllByAuthentication(Authentication authentication){
        return findAllByMasterId(masterService.getAuthenticatedMaster(authentication).getId());
    }

    public List<Category> findAllByMasterId(Long id){
        return categoryRepository.findByMasterId(id);
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow();
    }

}
