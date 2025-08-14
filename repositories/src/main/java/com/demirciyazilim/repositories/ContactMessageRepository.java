package com.demirciyazilim.repositories;

import com.demirciyazilim.entities.ContactMessage;
import com.demirciyazilim.entities.enums.ContactMessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByStatus(ContactMessageStatus status, Pageable pageable);
}


