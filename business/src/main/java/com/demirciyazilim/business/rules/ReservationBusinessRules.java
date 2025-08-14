package com.demirciyazilim.business.rules;

import com.demirciyazilim.core.utilities.exceptions.BusinessException;
import com.demirciyazilim.entities.enums.ReservationStatus;
import com.demirciyazilim.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ReservationBusinessRules {
    
    private final ReservationRepository reservationRepository;
    
    // Temel validasyonlar
    public void checkIfReservationExists(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new BusinessException("Rezervasyon bulunamadı");
        }
    }
    
    public void checkIfDateIsNotPast(LocalDate reservationDate) {
        if (reservationDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Geçmiş tarihlere rezervasyon yapılamaz");
        }
    }
    
    public void checkIfTimeIsValid(LocalDate reservationDate, LocalTime reservationTime) {
        // Bugün için rezervasyon yapılıyorsa, saatin geçmemiş olması gerekir
        if (reservationDate.equals(LocalDate.now()) && reservationTime.isBefore(LocalTime.now())) {
            throw new BusinessException("Geçmiş saatlere rezervasyon yapılamaz");
        }
    }
    
    // Çalışma saatleri kontrolü
    public void checkIfRestaurantIsOpen(LocalTime reservationTime) {
        LocalTime openingTime = LocalTime.of(11, 0); // 11:00
        LocalTime closingTime = LocalTime.of(23, 0); // 23:00
        
        if (reservationTime.isBefore(openingTime) || reservationTime.isAfter(closingTime)) {
            throw new BusinessException("Restaurant çalışma saatleri: 11:00 - 23:00 arası");
        }
    }
    
    // Kapasite kontrolü
    public void checkCapacity(LocalDate date, LocalTime time, Integer guestCount) {
        Integer maxCapacity = 100; // Restaurant kapasitesi
        Integer maxReservationsPerSlot = 10; // Aynı saatte maksimum rezervasyon sayısı
        
        Integer currentGuestCount = reservationRepository.getTotalGuestCountByDateAndTime(date, time);
        Integer currentReservationCount = reservationRepository.getReservationCountByDateAndTime(date, time);
        
        if (currentGuestCount != null && (currentGuestCount + guestCount) > maxCapacity) {
            throw new BusinessException("Bu tarih ve saatte kapasite dolu. Mevcut kapasite: " + 
                (maxCapacity - currentGuestCount) + " kişi");
        }
        
        if (currentReservationCount != null && currentReservationCount >= maxReservationsPerSlot) {
            throw new BusinessException("Bu tarih ve saatte maksimum rezervasyon sayısına ulaşıldı");
        }
    }
    
    // Masa çakışma kontrolü
    public void checkTableAvailability(LocalDate date, LocalTime time, String tableNumber) {
        if (tableNumber != null && !tableNumber.trim().isEmpty()) {
            var conflictingReservations = reservationRepository.findConflictingReservations(date, time, tableNumber);
            if (!conflictingReservations.isEmpty()) {
                throw new BusinessException("Bu tarih ve saatte " + tableNumber + " numaralı masa müsait değil");
            }
        }
    }
    
    // Güncelleme için masa çakışma kontrolü
    public void checkTableAvailabilityForUpdate(LocalDate date, LocalTime time, String tableNumber, Long reservationId) {
        if (tableNumber != null && !tableNumber.trim().isEmpty()) {
            var conflictingReservations = reservationRepository.findConflictingReservations(date, time, tableNumber);
            // Kendi rezervasyonu hariç çakışan rezervasyon var mı kontrol et
            boolean hasConflict = conflictingReservations.stream()
                    .anyMatch(r -> !r.getId().equals(reservationId));
            
            if (hasConflict) {
                throw new BusinessException("Bu tarih ve saatte " + tableNumber + " numaralı masa müsait değil");
            }
        }
    }
    
    // Durum değişikliği kontrolü
    public void checkIfStatusChangeIsValid(ReservationStatus currentStatus, ReservationStatus newStatus) {
        // PENDING -> CONFIRMED, CANCELLED
        // CONFIRMED -> COMPLETED, CANCELLED, NO_SHOW
        // CANCELLED -> Değiştirilemez
        // COMPLETED -> Değiştirilemez
        // NO_SHOW -> Değiştirilemez
        
        switch (currentStatus) {
            case PENDING:
                if (newStatus != ReservationStatus.CONFIRMED && 
                    newStatus != ReservationStatus.CANCELLED) {
                    throw new BusinessException("Beklemede olan rezervasyon sadece onaylanabilir veya iptal edilebilir");
                }
                break;
            case CONFIRMED:
                if (newStatus != ReservationStatus.COMPLETED && 
                    newStatus != ReservationStatus.CANCELLED && 
                    newStatus != ReservationStatus.NO_SHOW) {
                    throw new BusinessException("Onaylanmış rezervasyon sadece tamamlanabilir, iptal edilebilir veya gelmedi olarak işaretlenebilir");
                }
                break;
            case CANCELLED:
            case COMPLETED:
            case NO_SHOW:
                throw new BusinessException("Bu durumdaki rezervasyonun durumu değiştirilemez");
        }
    }
    
    // Gelecek tarih kontrolü (sadece oluşturma için)
    public void checkIfDateIsInFuture(LocalDate reservationDate) {
        if (reservationDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Rezervasyon tarihi bugünden sonra olmalıdır");
        }
    }
    
    // Müşteri email kontrolü (opsiyonel - aynı email ile çakışan rezervasyon)
    public void checkCustomerEmailConflict(String email, LocalDate date, LocalTime time) {
        var customerReservations = reservationRepository.findByCustomerEmailOrderByReservationDateDescReservationTimeDesc(email);
        
        boolean hasConflictingReservation = customerReservations.stream()
                .anyMatch(r -> r.getReservationDate().equals(date) && 
                              r.getReservationTime().equals(time) &&
                              (r.getStatus() == ReservationStatus.PENDING || r.getStatus() == ReservationStatus.CONFIRMED));
        
        if (hasConflictingReservation) {
            throw new BusinessException("Bu email adresi ile aynı tarih ve saatte zaten bir rezervasyonunuz bulunmaktadır");
        }
    }
    
    // Güncelleme için müşteri email kontrolü
    public void checkCustomerEmailConflictForUpdate(String email, LocalDate date, LocalTime time, Long reservationId) {
        var customerReservations = reservationRepository.findByCustomerEmailOrderByReservationDateDescReservationTimeDesc(email);
        
        boolean hasConflictingReservation = customerReservations.stream()
                .anyMatch(r -> r.getReservationDate().equals(date) && 
                              r.getReservationTime().equals(time) &&
                              (r.getStatus() == ReservationStatus.PENDING || r.getStatus() == ReservationStatus.CONFIRMED) &&
                              !r.getId().equals(reservationId));
        
        if (hasConflictingReservation) {
            throw new BusinessException("Bu email adresi ile aynı tarih ve saatte zaten başka bir rezervasyonunuz bulunmaktadır");
        }
    }
}
