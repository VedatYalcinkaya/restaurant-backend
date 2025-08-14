package com.demirciyazilim.business.dtos.reservation.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSummaryResponse {
    
    private Long totalReservations;
    private Long pendingReservations;
    private Long confirmedReservations;
    private Long cancelledReservations;
    private Integer todayTotalGuests;
    private Long todayReservationCount;
}
