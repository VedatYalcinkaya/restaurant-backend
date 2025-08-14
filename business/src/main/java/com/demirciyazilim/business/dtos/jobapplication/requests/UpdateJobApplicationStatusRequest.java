package com.demirciyazilim.business.dtos.jobapplication.requests;

import com.demirciyazilim.entities.enums.JobApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobApplicationStatusRequest {
    @NotNull
    private JobApplicationStatus status;

    @Size(max = 1000)
    private String notes;
}



