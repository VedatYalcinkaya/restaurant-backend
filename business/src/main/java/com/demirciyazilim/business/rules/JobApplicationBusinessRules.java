package com.demirciyazilim.business.rules;

import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.repositories.JobPositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobApplicationBusinessRules {

    private final JobPositionRepository jobPositionRepository;

    public void checkIfPositionExists(Long positionId) {
        if (!jobPositionRepository.existsById(positionId)) {
            throw new BusinessException("İlan bulunamadı");
        }
    }

    public void checkGdprConsent(Boolean consent) {
        if (consent == null || !consent) {
            throw new BusinessException("KVKK/GDPR onayı zorunludur");
        }
    }
}



