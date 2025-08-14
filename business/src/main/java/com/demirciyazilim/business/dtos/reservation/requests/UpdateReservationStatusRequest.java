package com.demirciyazilim.business.dtos.reservation.requests;

import com.demirciyazilim.entities.enums.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationStatusRequest {
    
    @NotNull(message = "Rezervasyon durumu boş olamaz")
    private ReservationStatus status;
    
    @Size(max = 500, message = "Admin notları en fazla 500 karakter olmalıdır")
    private String adminNotes;
}
