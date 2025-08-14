package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
import com.demirciyazilim.business.dtos.jobapplication.requests.CreateJobApplicationRequest;
import com.demirciyazilim.business.dtos.jobapplication.responses.JobApplicationResponse;
import com.demirciyazilim.entities.JobApplication;
import com.demirciyazilim.entities.JobPosition;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JobApplicationMapper {

    private final JobPositionMapper jobPositionMapper;

    public JobApplication toEntity(CreateJobApplicationRequest request) {
        JobApplication entity = new JobApplication();
        entity.setApplicantName(request.getApplicantName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setCoverLetter(request.getCoverLetter());
        entity.setGdprConsent(Boolean.TRUE.equals(request.getGdprConsent()));
        entity.setResumeUrl(request.getResumeUrl());

        JobPosition position = new JobPosition();
        position.setId(request.getPositionId());
        entity.setPosition(position);

        return entity;
    }

    public JobApplicationResponse toResponse(JobApplication entity) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setId(entity.getId());
        JobPositionResponse pos = jobPositionMapper.toResponse(entity.getPosition());
        response.setPosition(pos);
        response.setApplicantName(entity.getApplicantName());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        response.setCoverLetter(entity.getCoverLetter());
        response.setResumeUrl(entity.getResumeUrl());
        response.setStatus(entity.getStatus());
        response.setNotes(entity.getNotes());
        response.setGdprConsent(entity.isGdprConsent());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public List<JobApplicationResponse> toResponseList(List<JobApplication> entities) {
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}



