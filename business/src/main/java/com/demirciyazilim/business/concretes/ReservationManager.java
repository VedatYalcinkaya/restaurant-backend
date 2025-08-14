package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.ReservationService;
import com.demirciyazilim.business.dtos.reservation.requests.CreateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationRequest;
import com.demirciyazilim.business.dtos.reservation.requests.UpdateReservationStatusRequest;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationResponse;
import com.demirciyazilim.business.dtos.reservation.responses.ReservationSummaryResponse;
import com.demirciyazilim.business.mappers.ReservationMapper;
import com.demirciyazilim.business.rules.ReservationBusinessRules;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.Reservation;
import com.demirciyazilim.entities.enums.ReservationStatus;
import com.demirciyazilim.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationManager implements ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final ReservationBusinessRules reservationBusinessRules;
    private final ReservationMapper reservationMapper;
    
    @Override
    public DataResult<ReservationResponse> add(CreateReservationRequest createReservationRequest) {
        // Business rules
        reservationBusinessRules.checkIfDateIsInFuture(createReservationRequest.getReservationDate());
        reservationBusinessRules.checkIfTimeIsValid(createReservationRequest.getReservationDate(), createReservationRequest.getReservationTime());
        reservationBusinessRules.checkIfRestaurantIsOpen(createReservationRequest.getReservationTime());
        reservationBusinessRules.checkCapacity(createReservationRequest.getReservationDate(), createReservationRequest.getReservationTime(), createReservationRequest.getGuestCount());
        reservationBusinessRules.checkTableAvailability(createReservationRequest.getReservationDate(), createReservationRequest.getReservationTime(), createReservationRequest.getTableNumber());
        reservationBusinessRules.checkCustomerEmailConflict(createReservationRequest.getCustomerEmail(), createReservationRequest.getReservationDate(), createReservationRequest.getReservationTime());
        
        Reservation reservation = reservationMapper.toEntity(createReservationRequest);
        reservation.setCreatedAt(LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationResponse reservationResponse = reservationMapper.toResponse(savedReservation);
        return new SuccessDataResult<>(reservationResponse, "Rezervasyon başarıyla oluşturuldu");
    }
    
    @Override
    public DataResult<ReservationResponse> update(Long id, UpdateReservationRequest updateReservationRequest) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation existingReservation = reservationRepository.findById(id).get();
        
        // Business rules
        reservationBusinessRules.checkIfTimeIsValid(updateReservationRequest.getReservationDate(), updateReservationRequest.getReservationTime());
        reservationBusinessRules.checkIfRestaurantIsOpen(updateReservationRequest.getReservationTime());
        reservationBusinessRules.checkIfStatusChangeIsValid(existingReservation.getStatus(), updateReservationRequest.getStatus());
        reservationBusinessRules.checkTableAvailabilityForUpdate(updateReservationRequest.getReservationDate(), updateReservationRequest.getReservationTime(), updateReservationRequest.getTableNumber(), id);
        reservationBusinessRules.checkCustomerEmailConflictForUpdate(updateReservationRequest.getCustomerEmail(), updateReservationRequest.getReservationDate(), updateReservationRequest.getReservationTime(), id);
        
        reservationMapper.updateEntityFromRequest(updateReservationRequest, existingReservation);
        existingReservation.setUpdatedAt(LocalDateTime.now());
        
        // Status değişikliklerinde timestamp güncelleme
        if (updateReservationRequest.getStatus() == ReservationStatus.CONFIRMED && existingReservation.getConfirmedAt() == null) {
            existingReservation.setConfirmedAt(LocalDateTime.now());
        } else if (updateReservationRequest.getStatus() == ReservationStatus.CANCELLED && existingReservation.getCancelledAt() == null) {
            existingReservation.setCancelledAt(LocalDateTime.now());
        }
        
        Reservation updatedReservation = reservationRepository.save(existingReservation);
        ReservationResponse reservationResponse = reservationMapper.toResponse(updatedReservation);
        return new SuccessDataResult<>(reservationResponse, "Rezervasyon başarıyla güncellendi");
    }
    
    @Override
    public DataResult<ReservationResponse> updateStatus(Long id, UpdateReservationStatusRequest updateReservationStatusRequest) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation existingReservation = reservationRepository.findById(id).get();
        reservationBusinessRules.checkIfStatusChangeIsValid(existingReservation.getStatus(), updateReservationStatusRequest.getStatus());
        
        existingReservation.setStatus(updateReservationStatusRequest.getStatus());
        existingReservation.setAdminNotes(updateReservationStatusRequest.getAdminNotes());
        existingReservation.setUpdatedAt(LocalDateTime.now());
        
        // Status değişikliklerinde timestamp güncelleme
        if (updateReservationStatusRequest.getStatus() == ReservationStatus.CONFIRMED && existingReservation.getConfirmedAt() == null) {
            existingReservation.setConfirmedAt(LocalDateTime.now());
        } else if (updateReservationStatusRequest.getStatus() == ReservationStatus.CANCELLED && existingReservation.getCancelledAt() == null) {
            existingReservation.setCancelledAt(LocalDateTime.now());
        }
        
        Reservation updatedReservation = reservationRepository.save(existingReservation);
        ReservationResponse reservationResponse = reservationMapper.toResponse(updatedReservation);
        return new SuccessDataResult<>(reservationResponse, "Rezervasyon durumu başarıyla güncellendi");
    }
    
    @Override
    public Result delete(Long id) {
        reservationBusinessRules.checkIfReservationExists(id);
        reservationRepository.deleteById(id);
        return new SuccessResult("Rezervasyon başarıyla silindi");
    }
    
    @Override
    public DataResult<ReservationResponse> getById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            return new ErrorDataResult<>("Rezervasyon bulunamadı");
        }
        ReservationResponse reservationResponse = reservationMapper.toResponse(reservation.get());
        return new SuccessDataResult<>(reservationResponse, "Rezervasyon başarıyla getirildi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationDate", "reservationTime"));
        Page<Reservation> result = reservationRepository.findAll(pageable);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(reservationResponses, "Rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByStatus(ReservationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationDate", "reservationTime"));
        Page<Reservation> result = reservationRepository.findByStatus(status, pageable);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(reservationResponses, "Durum bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByReservationDateOrderByReservationTimeAsc(date);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Tarih bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByDateAndStatus(LocalDate date, ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByReservationDateAndStatusOrderByReservationTimeAsc(date, status);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Tarih ve durum bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationRepository.findByDateRange(startDate, endDate);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Tarih aralığı bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByDateRangeAndStatus(startDate, endDate, status);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Tarih aralığı ve durum bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByCustomerEmail(String email) {
        List<Reservation> reservations = reservationRepository.findByCustomerEmailOrderByReservationDateDescReservationTimeDesc(email);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Müşteri email bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getByCustomerPhone(String phone) {
        List<Reservation> reservations = reservationRepository.findByCustomerPhoneOrderByReservationDateDescReservationTimeDesc(phone);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Müşteri telefon bazlı rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getTodayReservations() {
        List<Reservation> reservations = reservationRepository.findTodayReservations();
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Bugünkü rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getTomorrowReservations() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Reservation> reservations = reservationRepository.findTomorrowReservations(tomorrow);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(reservations);
        return new SuccessDataResult<>(reservationResponses, "Yarınki rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getUpcomingReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> result = reservationRepository.findUpcomingReservations(pageable);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(reservationResponses, "Gelecek rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public DataResult<List<ReservationResponse>> getPastReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> result = reservationRepository.findPastReservations(pageable);
        List<ReservationResponse> reservationResponses = reservationMapper.toResponseList(result.getContent());
        return new SuccessDataResult<>(reservationResponses, "Geçmiş rezervasyonlar başarıyla listelendi");
    }
    
    @Override
    public Result confirmReservation(Long id) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation reservation = reservationRepository.findById(id).get();
        reservationBusinessRules.checkIfStatusChangeIsValid(reservation.getStatus(), ReservationStatus.CONFIRMED);
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setConfirmedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        
        return new SuccessResult("Rezervasyon başarıyla onaylandı");
    }
    
    @Override
    public Result cancelReservation(Long id) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation reservation = reservationRepository.findById(id).get();
        reservationBusinessRules.checkIfStatusChangeIsValid(reservation.getStatus(), ReservationStatus.CANCELLED);
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        
        return new SuccessResult("Rezervasyon başarıyla iptal edildi");
    }
    
    @Override
    public Result completeReservation(Long id) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation reservation = reservationRepository.findById(id).get();
        reservationBusinessRules.checkIfStatusChangeIsValid(reservation.getStatus(), ReservationStatus.COMPLETED);
        
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        
        return new SuccessResult("Rezervasyon başarıyla tamamlandı");
    }
    
    @Override
    public Result markAsNoShow(Long id) {
        reservationBusinessRules.checkIfReservationExists(id);
        
        Reservation reservation = reservationRepository.findById(id).get();
        reservationBusinessRules.checkIfStatusChangeIsValid(reservation.getStatus(), ReservationStatus.NO_SHOW);
        
        reservation.setStatus(ReservationStatus.NO_SHOW);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        
        return new SuccessResult("Rezervasyon 'gelmedi' olarak işaretlendi");
    }
    
    @Override
    public DataResult<ReservationSummaryResponse> getReservationSummary() {
        Long totalReservations = reservationRepository.count();
        Long pendingReservations = reservationRepository.findByStatusOrderByReservationDateDescReservationTimeDesc(ReservationStatus.PENDING).size() + 0L;
        Long confirmedReservations = reservationRepository.findByStatusOrderByReservationDateDescReservationTimeDesc(ReservationStatus.CONFIRMED).size() + 0L;
        Long cancelledReservations = reservationRepository.findByStatusOrderByReservationDateDescReservationTimeDesc(ReservationStatus.CANCELLED).size() + 0L;
        Integer todayTotalGuests = reservationRepository.getTodayConfirmedGuestCount();
        Long todayReservationCount = reservationRepository.getTodayConfirmedReservationCount();
        
        ReservationSummaryResponse summary = new ReservationSummaryResponse(
                totalReservations,
                pendingReservations,
                confirmedReservations,
                cancelledReservations,
                todayTotalGuests != null ? todayTotalGuests : 0,
                todayReservationCount != null ? todayReservationCount : 0L
        );
        
        return new SuccessDataResult<>(summary, "Rezervasyon özeti başarıyla getirildi");
    }

    @Override
    public Result deleteAllFinalized() {
        int deleted = reservationRepository.deleteAllFinalizedReservations();
        return new SuccessResult("Toplam " + deleted + " tamamlanan/iptal edilen/gelmeyen rezervasyon silindi");
    }
}
