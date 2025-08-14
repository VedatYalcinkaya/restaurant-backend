package com.demirciyazilim.entities;

import com.demirciyazilim.core.entities.BaseEntity;
import com.demirciyazilim.entities.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String customerName;
    
    @Column(nullable = false, length = 100)
    private String customerSurname;
    
    @Column(nullable = false, length = 15)
    private String customerPhone;
    
    @Column(nullable = false, length = 150)
    private String customerEmail;
    
    @Column(nullable = false)
    private LocalDate reservationDate;
    
    @Column(nullable = false)
    private LocalTime reservationTime;
    
    @Column(nullable = false)
    private Integer guestCount;
    
    @Column(length = 20)
    private String tableNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;
    
    @Column(length = 500)
    private String specialRequests;
    
    @Column(length = 500)
    private String adminNotes;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @Column
    private LocalDateTime confirmedAt;
    
    @Column
    private LocalDateTime cancelledAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
