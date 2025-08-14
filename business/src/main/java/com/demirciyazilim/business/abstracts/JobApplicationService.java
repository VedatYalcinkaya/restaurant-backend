package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.jobapplication.requests.CreateJobApplicationRequest;
import com.demirciyazilim.business.dtos.jobapplication.requests.UpdateJobApplicationStatusRequest;
import com.demirciyazilim.business.dtos.jobapplication.responses.JobApplicationResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.JobApplicationStatus;

import java.util.List;

public interface JobApplicationService {
    DataResult<JobApplicationResponse> apply(CreateJobApplicationRequest request);
    DataResult<JobApplicationResponse> getById(Long id);
    DataResult<List<JobApplicationResponse>> getAll(int page, int size);
    DataResult<List<JobApplicationResponse>> getByStatus(JobApplicationStatus status, int page, int size);
    Result updateStatus(Long id, UpdateJobApplicationStatusRequest request);
    Result delete(Long id);
}



