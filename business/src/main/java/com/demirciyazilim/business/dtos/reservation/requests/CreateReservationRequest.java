package com.demirciyazilim.business.dtos.reservation.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequest {
    
    @NotBlank(message = "Müşteri adı boş olamaz")
    @Size(min = 2, max = 100, message = "Müşteri adı 2-100 karakter arasında olmalıdır")
    private String customerName;
    
    @NotBlank(message = "Müşteri soyadı boş olamaz")
    @Size(min = 2, max = 100, message = "Müşteri soyadı 2-100 karakter arasında olmalıdır")
    private String customerSurname;
    
    @NotBlank(message = "Telefon numarası boş olamaz")
    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Geçerli bir telefon numarası giriniz")
    @Size(min = 10, max = 15, message = "Telefon numarası 10-15 karakter arasında olmalıdır")
    private String customerPhone;
    
    @NotBlank(message = "Email adresi boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Size(max = 150, message = "Email adresi en fazla 150 karakter olmalıdır")
    private String customerEmail;
    
    @NotNull(message = "Rezervasyon tarihi boş olamaz")
    @Future(message = "Rezervasyon tarihi gelecek bir tarih olmalıdır")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    
    @NotNull(message = "Rezervasyon saati boş olamaz")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime reservationTime;
    
    @NotNull(message = "Kişi sayısı boş olamaz")
    @Min(value = 1, message = "Kişi sayısı en az 1 olmalıdır")
    @Max(value = 20, message = "Kişi sayısı en fazla 20 olmalıdır")
    private Integer guestCount;
    
    @Size(max = 20, message = "Masa numarası en fazla 20 karakter olmalıdır")
    private String tableNumber;
    
    @Size(max = 500, message = "Özel istekler en fazla 500 karakter olmalıdır")
    private String specialRequests;
}
