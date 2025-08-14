package com.demirciyazilim.entities;

import com.demirciyazilim.core.entities.BaseEntity;
import com.demirciyazilim.entities.enums.JobApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private JobPosition position;

    @Column(nullable = false, length = 120)
    private String applicantName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 25)
    private String phone;

    @Column(length = 2000)
    private String coverLetter;

    @Column(length = 500)
    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobApplicationStatus status = JobApplicationStatus.RECEIVED;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean gdprConsent = true;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}



