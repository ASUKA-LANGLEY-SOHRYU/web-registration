package com.prosvirnin.webregistration.model.user.dto;

import com.prosvirnin.webregistration.model.service.Category;
import com.prosvirnin.webregistration.model.service.Service;
import com.prosvirnin.webregistration.model.service.dto.CategoryDTO;
import com.prosvirnin.webregistration.model.service.dto.ScheduleResponse;
import com.prosvirnin.webregistration.model.service.dto.ServiceDTOResponse;
import com.prosvirnin.webregistration.model.user.Address;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MasterProfile {
    private String fullName;
    private String description;
    private Address address;
    private String messenger;
    private Long profilePictureId;
    private List<Long> additionalImagesIds;
    private String phoneNumber;
    private List<ServiceDTOResponse> services;
    private List<Category> categories;
    private List<ScheduleResponse> schedule;
}
