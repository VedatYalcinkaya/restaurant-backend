package com.demirciyazilim.business.dtos.jobapplication.responses;

import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
import com.demirciyazilim.entities.enums.JobApplicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private Long id;
    private JobPositionResponse position;
    private String applicantName;
    private String email;
    private String phone;
    private String coverLetter;
    private String resumeUrl;
    private JobApplicationStatus status;
    private String notes;
    private boolean gdprConsent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updatedAt;
}



