package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.JobApplication;
import com.demirciyazilim.entities.enums.JobApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByStatus(JobApplicationStatus status, Pageable pageable);

    @Query("SELECT COUNT(j) FROM JobApplication j WHERE j.status = 'RECEIVED'")
    Long countNewApplications();
}



