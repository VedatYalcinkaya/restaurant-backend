package com.demirciyazilim.business.dtos.contactmessage.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateContactMessageRequest {
    @NotBlank
    @Size(min = 2, max = 120)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 25)
    @Pattern(regexp = "^[0-9+\\-\\s()]+$")
    private String phone;

    @Size(max = 150)
    private String subject;

    @NotBlank
    @Size(min = 5, max = 2000)
    private String message;
}


