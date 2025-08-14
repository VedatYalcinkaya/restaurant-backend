package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.job.requests.CreateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.requests.UpdateJobPositionRequest;
import com.demirciyazilim.business.dtos.job.responses.JobPositionResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;

import java.util.List;

public interface JobPositionService {
    DataResult<List<JobPositionResponse>> getAll();
    DataResult<List<JobPositionResponse>> getActive();
    DataResult<JobPositionResponse> getById(Long id);
    DataResult<JobPositionResponse> add(CreateJobPositionRequest request);
    DataResult<JobPositionResponse> update(Long id, UpdateJobPositionRequest request);
    Result delete(Long id);
    Result activate(Long id);
    Result deactivate(Long id);
}



