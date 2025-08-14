package com.demirciyazilim.business.dtos.jobapplication.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobApplicationRequest {

    @NotNull
    private Long positionId;

    @NotBlank
    @Size(min = 2, max = 120)
    private String applicantName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 25)
    @Pattern(regexp = "^[0-9+\\-\\s()]+$")
    private String phone;

    @Size(max = 2000)
    private String coverLetter;

    @NotNull
    private Boolean gdprConsent;

    @Size(max = 500)
    private String resumeUrl;
}



