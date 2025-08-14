package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.JobApplicationService;
import com.demirciyazilim.business.dtos.jobapplication.requests.CreateJobApplicationRequest;
import com.demirciyazilim.business.dtos.jobapplication.requests.UpdateJobApplicationStatusRequest;
import com.demirciyazilim.business.dtos.jobapplication.responses.JobApplicationResponse;
import com.demirciyazilim.business.mappers.JobApplicationMapper;
import com.demirciyazilim.business.rules.JobApplicationBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.JobApplication;
import com.demirciyazilim.entities.JobPosition;
import com.demirciyazilim.entities.enums.JobApplicationStatus;
import com.demirciyazilim.repositories.JobApplicationRepository;
import com.demirciyazilim.repositories.JobPositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobApplicationManager implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPositionRepository jobPositionRepository;
    private final JobApplicationBusinessRules jobApplicationBusinessRules;
    private final JobApplicationMapper jobApplicationMapper;

    @Override
    public DataResult<JobApplicationResponse> apply(CreateJobApplicationRequest request) {
        jobApplicationBusinessRules.checkIfPositionExists(request.getPositionId());
        jobApplicationBusinessRules.checkGdprConsent(request.getGdprConsent());

        JobApplication entity = jobApplicationMapper.toEntity(request);
        // Pozisyonu repository'den yükleyelim
        JobPosition position = jobPositionRepository.findById(request.getPositionId()).get();
        entity.setPosition(position);
        entity.setCreatedAt(LocalDateTime.now());
        JobApplication saved = jobApplicationRepository.save(entity);
        return new SuccessDataResult<>(jobApplicationMapper.toResponse(saved), "Başvurunuz alındı");
    }

    @Override
    public DataResult<JobApplicationResponse> getById(Long id) {
        Optional<JobApplication> app = jobApplicationRepository.findById(id);
        if (app.isEmpty()) {
            return new ErrorDataResult<>("Başvuru bulunamadı");
        }
        return new SuccessDataResult<>(jobApplicationMapper.toResponse(app.get()), "Başvuru getirildi");
    }

    @Override
    public DataResult<List<JobApplicationResponse>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobApplication> result = jobApplicationRepository.findAll(pageable);
        List<JobApplicationResponse> responses = jobApplicationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(responses, "Başvurular listelendi");
    }

    @Override
    public DataResult<List<JobApplicationResponse>> getByStatus(JobApplicationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<JobApplication> result = jobApplicationRepository.findByStatus(status, pageable);
        List<JobApplicationResponse> responses = jobApplicationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(responses, "Duruma göre başvurular listelendi");
    }

    @Override
    public Result updateStatus(Long id, UpdateJobApplicationStatusRequest request) {
        Optional<JobApplication> appOpt = jobApplicationRepository.findById(id);
        if (appOpt.isEmpty()) {
            return new ErrorResult("Başvuru bulunamadı");
        }
        JobApplication app = appOpt.get();
        app.setStatus(request.getStatus());
        app.setNotes(request.getNotes());
        app.setUpdatedAt(LocalDateTime.now());
        jobApplicationRepository.save(app);
        return new SuccessResult("Başvuru durumu güncellendi");
    }

    @Override
    public Result delete(Long id) {
        if (!jobApplicationRepository.existsById(id)) {
            return new ErrorResult("Başvuru bulunamadı");
        }
        jobApplicationRepository.deleteById(id);
        return new SuccessResult("Başvuru silindi");
    }
}



