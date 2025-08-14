package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    List<JobPosition> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<JobPosition> findAllByOrderByDisplayOrderAsc();

    Page<JobPosition> findByIsActiveTrue(Pageable pageable);
}



