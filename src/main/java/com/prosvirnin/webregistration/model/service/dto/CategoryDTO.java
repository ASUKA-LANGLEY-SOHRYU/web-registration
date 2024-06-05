package com.prosvirnin.webregistration.model.service.dto;

import com.prosvirnin.webregistration.core.Mapper;
import com.prosvirnin.webregistration.model.service.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO implements Mapper<Category> {
    private String name;
    private String description;

    @Override
    public Category map() {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
