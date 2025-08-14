package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.Reservation;
import com.demirciyazilim.entities.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Tarih bazlı sorgular
    List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate date);
    
    List<Reservation> findByReservationDateAndStatusOrderByReservationTimeAsc(LocalDate date, ReservationStatus status);
    
    // Durum bazlı sorgular
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
    
    List<Reservation> findByStatusOrderByReservationDateDescReservationTimeDesc(ReservationStatus status);
    
    // Müşteri bazlı sorgular
    List<Reservation> findByCustomerEmailOrderByReservationDateDescReservationTimeDesc(String email);
    
    List<Reservation> findByCustomerPhoneOrderByReservationDateDescReservationTimeDesc(String phone);
    
    // Tarih aralığı sorguları
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate ORDER BY r.reservationDate ASC, r.reservationTime ASC")
    List<Reservation> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate AND r.status = :status ORDER BY r.reservationDate ASC, r.reservationTime ASC")
    List<Reservation> findByDateRangeAndStatus(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("status") ReservationStatus status);
    
    // Kapasite kontrolü için
    @Query("SELECT SUM(r.guestCount) FROM Reservation r WHERE r.reservationDate = :date AND r.reservationTime = :time AND r.status IN ('PENDING', 'CONFIRMED')")
    Integer getTotalGuestCountByDateAndTime(@Param("date") LocalDate date, @Param("time") LocalTime time);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = :date AND r.reservationTime = :time AND r.status IN ('PENDING', 'CONFIRMED')")
    Integer getReservationCountByDateAndTime(@Param("date") LocalDate date, @Param("time") LocalTime time);
    
    // Çakışma kontrolü
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date AND r.reservationTime = :time AND r.tableNumber = :tableNumber AND r.status IN ('PENDING', 'CONFIRMED')")
    List<Reservation> findConflictingReservations(@Param("date") LocalDate date, @Param("time") LocalTime time, @Param("tableNumber") String tableNumber);
    
    // Bugünkü rezervasyonlar
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = CURRENT_DATE ORDER BY r.reservationTime ASC")
    List<Reservation> findTodayReservations();
    
    // Yarınki rezervasyonlar
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :tomorrow ORDER BY r.reservationTime ASC")
    List<Reservation> findTomorrowReservations(@Param("tomorrow") LocalDate tomorrow);
    
    // Geçmiş rezervasyonlar
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate < CURRENT_DATE ORDER BY r.reservationDate DESC, r.reservationTime DESC")
    Page<Reservation> findPastReservations(Pageable pageable);
    
    // Gelecek rezervasyonlar
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate >= CURRENT_DATE ORDER BY r.reservationDate ASC, r.reservationTime ASC")
    Page<Reservation> findUpcomingReservations(Pageable pageable);
    
    // İstatistikler için
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = CURRENT_DATE AND r.status = 'CONFIRMED'")
    Long getTodayConfirmedReservationCount();
    
    @Query("SELECT SUM(r.guestCount) FROM Reservation r WHERE r.reservationDate = CURRENT_DATE AND r.status = 'CONFIRMED'")
    Integer getTodayConfirmedGuestCount();

    // Finalize olmuş rezervasyonları toplu silme
    @Modifying
    @Transactional
    @Query("DELETE FROM Reservation r WHERE r.status IN ('COMPLETED','CANCELLED','NO_SHOW')")
    int deleteAllFinalizedReservations();
}
