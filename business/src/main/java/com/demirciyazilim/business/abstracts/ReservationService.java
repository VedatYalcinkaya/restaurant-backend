package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.reservation.requests.CreateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationStatusRequest;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationResponse;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationSummaryResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    
    // CRUD Operations
    DataResult<ReservationResponse> add(CreateReservationRequest createReservationRequest);
    
    DataResult<ReservationResponse> update(Long id, UpdateReservationRequest updateReservationRequest);
    
    DataResult<ReservationResponse> updateStatus(Long id, UpdateReservationStatusRequest updateReservationStatusRequest);
    
    Result delete(Long id);
    
    DataResult<ReservationResponse> getById(Long id);
    
    // Listing Operations
    DataResult<List<ReservationResponse>> getAll(int page, int size);
    
    DataResult<List<ReservationResponse>> getByStatus(ReservationStatus status, int page, int size);
    
    DataResult<List<ReservationResponse>> getByDate(LocalDate date);
    
    DataResult<List<ReservationResponse>> getByDateAndStatus(LocalDate date, ReservationStatus status);
    
    DataResult<List<ReservationResponse>> getByDateRange(LocalDate startDate, LocalDate endDate);
    
    DataResult<List<ReservationResponse>> getByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, ReservationStatus status);
    
    // Customer Operations
    DataResult<List<ReservationResponse>> getByCustomerEmail(String email);
    
    DataResult<List<ReservationResponse>> getByCustomerPhone(String phone);
    
    // Time-based Operations
    DataResult<List<ReservationResponse>> getTodayReservations();
    
    DataResult<List<ReservationResponse>> getTomorrowReservations();
    
    DataResult<List<ReservationResponse>> getUpcomingReservations(int page, int size);
    
    DataResult<List<ReservationResponse>> getPastReservations(int page, int size);
    
    // Status Change Operations
    Result confirmReservation(Long id);
    
    Result cancelReservation(Long id);
    
    Result completeReservation(Long id);
    
    Result markAsNoShow(Long id);
    
    // Statistics
    DataResult<ReservationSummaryResponse> getReservationSummary();

    // Bulk delete finalized reservations
    Result deleteAllFinalized();
}
