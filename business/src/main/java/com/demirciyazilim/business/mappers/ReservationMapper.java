package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.reservation.requests.CreateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationResponse;
import com.demirciyazilim.entities.Reservation;
import com.demirciyazilim.entities.enums.ReservationStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {

    public Reservation toEntity(CreateReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerSurname(request.getCustomerSurname());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());
        reservation.setTableNumber(request.getTableNumber());
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setStatus(ReservationStatus.PENDING);
        return reservation;
    }
    
    public Reservation toEntity(UpdateReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setId(request.getId());
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerSurname(request.getCustomerSurname());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());
        reservation.setTableNumber(request.getTableNumber());
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setStatus(request.getStatus());
        reservation.setAdminNotes(request.getAdminNotes());
        return reservation;
    }

    public ReservationResponse toResponse(Reservation entity) {
        ReservationResponse response = new ReservationResponse();
        response.setId(entity.getId());
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerSurname(entity.getCustomerSurname());
        response.setCustomerPhone(entity.getCustomerPhone());
        response.setCustomerEmail(entity.getCustomerEmail());
        response.setReservationDate(entity.getReservationDate());
        response.setReservationTime(entity.getReservationTime());
        response.setGuestCount(entity.getGuestCount());
        response.setTableNumber(entity.getTableNumber());
        response.setStatus(entity.getStatus());
        response.setSpecialRequests(entity.getSpecialRequests());
        response.setAdminNotes(entity.getAdminNotes());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setConfirmedAt(entity.getConfirmedAt());
        response.setCancelledAt(entity.getCancelledAt());
        return response;
    }

    public List<ReservationResponse> toResponseList(List<Reservation> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(UpdateReservationRequest request, Reservation entity) {
        entity.setCustomerName(request.getCustomerName());
        entity.setCustomerSurname(request.getCustomerSurname());
        entity.setCustomerPhone(request.getCustomerPhone());
        entity.setCustomerEmail(request.getCustomerEmail());
        entity.setReservationDate(request.getReservationDate());
        entity.setReservationTime(request.getReservationTime());
        entity.setGuestCount(request.getGuestCount());
        entity.setTableNumber(request.getTableNumber());
        entity.setSpecialRequests(request.getSpecialRequests());
        entity.setStatus(request.getStatus());
        entity.setAdminNotes(request.getAdminNotes());
    }
}
