package com.prosvirnin.webregistration.model.service.category;

import com.prosvirnin.webregistration.core.Mapper;
import lombok.Data;

@Data
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
