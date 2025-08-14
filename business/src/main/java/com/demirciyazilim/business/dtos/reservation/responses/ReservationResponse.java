package com.demirciyazilim.business.dtos.reservation.responses;

import com.demirciyazilim.entities.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    
    private Long id;
    private String customerName;
    private String customerSurname;
    private String customerPhone;
    private String customerEmail;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime reservationTime;
    
    private Integer guestCount;
    private String tableNumber;
    private ReservationStatus status;
    private String specialRequests;
    private String adminNotes;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime updatedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime confirmedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime cancelledAt;
}
