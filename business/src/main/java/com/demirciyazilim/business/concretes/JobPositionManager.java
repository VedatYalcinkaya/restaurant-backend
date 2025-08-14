package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.JobPositionService;
import com.demirciyazilim.business.dtos.job.requests.CreateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.requests.UpdateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
import com.demirciyazilim.business.mappers.JobPositionMapper;
import com.demirciyazilim.business.rules.JobPositionBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.JobPosition;
import com.demirciyazilim.repositories.JobPositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobPositionManager implements JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final JobPositionBusinessRules jobPositionBusinessRules;
    private final JobPositionMapper jobPositionMapper;

    @Override
    public DataResult<List<JobPositionResponse>> getAll() {
        List<JobPosition> positions = jobPositionRepository.findAllByOrderByDisplayOrderAsc();
        return new SuccessDataResult<>(jobPositionMapper.toResponseList(positions), "İlanlar listelendi");
    }

    @Override
    public DataResult<List<JobPositionResponse>> getActive() {
        List<JobPosition> positions = jobPositionRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        return new SuccessDataResult<>(jobPositionMapper.toResponseList(positions), "Aktif ilanlar listelendi");
    }

    @Override
    public DataResult<JobPositionResponse> getById(Long id) {
        Optional<JobPosition> position = jobPositionRepository.findById(id);
        if (position.isEmpty()) {
            return new ErrorDataResult<>("İlan bulunamadı");
        }
        return new SuccessDataResult<>(jobPositionMapper.toResponse(position.get()), "İlan getirildi");
    }

    @Override
    public DataResult<JobPositionResponse> add(CreateJobPositionRequest request) {
        jobPositionBusinessRules.checkIfTitleExists(request.getTitle());
        JobPosition entity = jobPositionMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        JobPosition saved = jobPositionRepository.save(entity);
        return new SuccessDataResult<>(jobPositionMapper.toResponse(saved), "İlan eklendi");
    }

    @Override
    public DataResult<JobPositionResponse> update(Long id, UpdateJobPositionRequest request) {
        jobPositionBusinessRules.checkIfJobPositionExists(id);
        jobPositionBusinessRules.checkIfTitleExistsForUpdate(request.getTitle(), id);
        JobPosition existing = jobPositionRepository.findById(id).get();
        jobPositionMapper.updateEntityFromRequest(request, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        JobPosition updated = jobPositionRepository.save(existing);
        return new SuccessDataResult<>(jobPositionMapper.toResponse(updated), "İlan güncellendi");
    }

    @Override
    public Result delete(Long id) {
        jobPositionBusinessRules.checkIfJobPositionExists(id);
        jobPositionRepository.deleteById(id);
        return new SuccessResult("İlan silindi");
    }

    @Override
    public Result activate(Long id) {
        jobPositionBusinessRules.checkIfJobPositionExists(id);
        JobPosition existing = jobPositionRepository.findById(id).get();
        existing.setActive(true);
        existing.setUpdatedAt(LocalDateTime.now());
        jobPositionRepository.save(existing);
        return new SuccessResult("İlan aktifleştirildi");
    }

    @Override
    public Result deactivate(Long id) {
        jobPositionBusinessRules.checkIfJobPositionExists(id);
        JobPosition existing = jobPositionRepository.findById(id).get();
        existing.setActive(false);
        existing.setUpdatedAt(LocalDateTime.now());
        jobPositionRepository.save(existing);
        return new SuccessResult("İlan pasifleştirildi");
    }
}



