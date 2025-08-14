package com.demirciyazilim.business.dtos.job.requests;

import com.demirciyazilim.entities.enums.EmploymentType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobPositionRequest {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 150)
    private String title;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String location;

    @NotNull
    private EmploymentType employmentType;

    @Size(max = 10000)
    private String description;

    @Size(max = 10000)
    private String requirements;

    @Size(max = 10000)
    private String benefits;

    private int displayOrder;

    private boolean active;
}



