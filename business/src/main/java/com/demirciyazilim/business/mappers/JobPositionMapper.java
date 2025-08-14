package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.job.requests.CreateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.requests.UpdateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
import com.demirciyazilim.entities.JobPosition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobPositionMapper {

    public JobPosition toEntity(CreateJobPositionRequest request) {
        JobPosition entity = new JobPosition();
        entity.setTitle(request.getTitle());
        entity.setDepartment(request.getDepartment());
        entity.setLocation(request.getLocation());
        entity.setEmploymentType(request.getEmploymentType());
        entity.setDescription(request.getDescription());
        entity.setRequirements(request.getRequirements());
        entity.setBenefits(request.getBenefits());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setActive(request.isActive());
        return entity;
    }

    public void updateEntityFromRequest(UpdateJobPositionRequest request, JobPosition entity) {
        entity.setTitle(request.getTitle());
        entity.setDepartment(request.getDepartment());
        entity.setLocation(request.getLocation());
        entity.setEmploymentType(request.getEmploymentType());
        entity.setDescription(request.getDescription());
        entity.setRequirements(request.getRequirements());
        entity.setBenefits(request.getBenefits());
        entity.setDisplayOrder(request.getDisplayOrder());
        entity.setActive(request.isActive());
    }

    public JobPositionResponse toResponse(JobPosition entity) {
        JobPositionResponse response = new JobPositionResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDepartment(entity.getDepartment());
        response.setLocation(entity.getLocation());
        response.setEmploymentType(entity.getEmploymentType());
        response.setDescription(entity.getDescription());
        response.setRequirements(entity.getRequirements());
        response.setBenefits(entity.getBenefits());
        response.setDisplayOrder(entity.getDisplayOrder());
        response.setActive(entity.isActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public List<JobPositionResponse> toResponseList(List<JobPosition> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}



