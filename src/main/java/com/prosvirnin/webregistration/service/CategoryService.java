package com.prosvirnin.webregistration.service;

import com.prosvirnin.webregistration.model.service.category.Category;
import com.prosvirnin.webregistration.model.service.category.CategoryDTO;
import com.prosvirnin.webregistration.model.user.Master;
import com.prosvirnin.webregistration.model.user.User;
import com.prosvirnin.webregistration.repository.CategoryRepository;
import com.prosvirnin.webregistration.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MasterRepository masterRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, MasterRepository masterRepository) {
        this.categoryRepository = categoryRepository;
        this.masterRepository = masterRepository;
    }

    @Transactional
    public void save(Authentication authentication, Category category){
        category.setMaster(getAuthenticatedMaster(authentication));
        categoryRepository.save(category);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional
    public boolean edit(Authentication authentication, Long categoryId, CategoryDTO edited){
        var category = categoryRepository.findById(categoryId).orElseThrow();
        if (!getAuthenticatedMaster(authentication).getId().equals(category.getMaster().getId())){
            return false;
        }
        editCategory(category, edited);
        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public boolean delete(Authentication authentication, Long categoryId){
        var category = categoryRepository.findById(categoryId).orElseThrow();
        if (!getAuthenticatedMaster(authentication).getId().equals(category.getMaster().getId())){
            return false;
        }
        //TODO: каскадное удаление сервисов
        categoryRepository.deleteById(categoryId);
        return true;
    }

    @Transactional
    public void edit(Long categoryId, CategoryDTO edited){
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
        return findAllByMasterId(getAuthenticatedMaster(authentication).getId());
    }

    public List<Category> findAllByMasterId(Long id){
        return categoryRepository.findByMasterId(id);
    }

    private Master getAuthenticatedMaster(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return masterRepository.findById(user.getMaster().getId()).orElseThrow();
    }
}
