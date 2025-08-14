package com.demirciyazilim.business.rules;

import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.repositories.JobPositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobPositionBusinessRules {

    private final JobPositionRepository jobPositionRepository;

    public void checkIfJobPositionExists(Long id) {
        if (!jobPositionRepository.existsById(id)) {
            throw new BusinessException("İlan bulunamadı");
        }
    }

    public void checkIfTitleExists(String title) {
        if (jobPositionRepository.existsByTitle(title)) {
            throw new BusinessException("Bu ilan başlığı zaten mevcut: " + title);
        }
    }

    public void checkIfTitleExistsForUpdate(String title, Long id) {
        if (jobPositionRepository.existsByTitleAndIdNot(title, id)) {
            throw new BusinessException("Bu ilan başlığı başka bir ilanda kullanılıyor: " + title);
        }
    }
}



