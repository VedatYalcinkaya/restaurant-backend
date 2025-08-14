package com.demirciyazilim.entities;

import com.demirciyazilim.core.entities.BaseEntity;
import com.demirciyazilim.entities.enums.ContactMessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessage extends BaseEntity {

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 25)
    private String phone;

    @Column(length = 150)
    private String subject;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactMessageStatus status = ContactMessageStatus.NEW;

    @Column(length = 1000)
    private String adminNotes;

    @Column
    private LocalDateTime repliedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}


