package com.demirciyazilim.business.concretes;

import com.demirciyazilim.business.abstracts.ContactMessageService;
import com.demirciyazilim.business.dtos.contactmessage.requests.CreateContactMessageRequest;
import com.demirciyazilim.business.dtos.contactmessage.responses.ContactMessageResponse;
import com.demirciyazilim.business.mappers.ContactMessageMapper;
import com.demirciyazilim.business.services.EmailService;
import com.demirciyazilim.core.utilities.results.*;
import com.demirciyazilim.entities.ContactMessage;
import com.demirciyazilim.entities.enums.ContactMessageStatus;
import com.demirciyazilim.repositories.ContactMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactMessageManager implements ContactMessageService {

    private final ContactMessageRepository repository;
    private final ContactMessageMapper mapper;
    private final EmailService emailService;

    @Override
    public DataResult<ContactMessageResponse> createAndNotify(CreateContactMessageRequest request) {
        ContactMessage entity = mapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        ContactMessage saved = repository.save(entity);

        // e-posta gönderimi
        try {
            String html = emailService.createContactFormEmailContent(
                    request.getFullName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getSubject(),
                    request.getMessage()
            );
            emailService.sendContactFormEmail(request.getEmail(), request.getSubject(), html);
        } catch (Exception ignored) { }

        return new SuccessDataResult<>(mapper.toResponse(saved), "Mesajınız alındı");
    }

    @Override
    public DataResult<List<ContactMessageResponse>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactMessage> result = repository.findAll(pageable);
        return new SuccessDataResult<>(mapper.toResponseList(result.getContent()), "Mesajlar listelendi");
    }

    @Override
    public DataResult<List<ContactMessageResponse>> getByStatus(ContactMessageStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactMessage> result = repository.findByStatus(status, pageable);
        return new SuccessDataResult<>(mapper.toResponseList(result.getContent()), "Mesajlar listelendi");
    }

    @Override
    public Result markRead(Long id) {
        var opt = repository.findById(id);
        if (opt.isEmpty()) return new ErrorResult("Mesaj bulunamadı");
        ContactMessage m = opt.get();
        m.setStatus(ContactMessageStatus.READ);
        m.setUpdatedAt(LocalDateTime.now());
        repository.save(m);
        return new SuccessResult("Mesaj okundu işaretlendi");
    }

    @Override
    public Result reply(Long id, String adminNotes) {
        var opt = repository.findById(id);
        if (opt.isEmpty()) return new ErrorResult("Mesaj bulunamadı");
        ContactMessage m = opt.get();
        m.setStatus(ContactMessageStatus.REPLIED);
        m.setAdminNotes(adminNotes);
        m.setRepliedAt(LocalDateTime.now());
        repository.save(m);
        return new SuccessResult("Mesaj cevaplandı olarak işaretlendi");
    }

    @Override
    public Result delete(Long id) {
        if (!repository.existsById(id)) return new ErrorResult("Mesaj bulunamadı");
        repository.deleteById(id);
        return new SuccessResult("Mesaj silindi");
    }
}


