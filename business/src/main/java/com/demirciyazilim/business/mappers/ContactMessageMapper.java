package com.demirciyazilim.business.mappers;

import com.demirciyazilim.business.dtos.contactmessage.requests.CreateContactMessageRequest;
import com.demirciyazilim.business.dtos.contactmessage.responses.ContactMessageResponse;
import com.demirciyazilim.entities.ContactMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMessageMapper {

    public ContactMessage toEntity(CreateContactMessageRequest request) {
        ContactMessage entity = new ContactMessage();
        entity.setFullName(request.getFullName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setSubject(request.getSubject());
        entity.setMessage(request.getMessage());
        return entity;
    }

    public ContactMessageResponse toResponse(ContactMessage entity) {
        ContactMessageResponse r = new ContactMessageResponse();
        r.setId(entity.getId());
        r.setFullName(entity.getFullName());
        r.setEmail(entity.getEmail());
        r.setPhone(entity.getPhone());
        r.setSubject(entity.getSubject());
        r.setMessage(entity.getMessage());
        r.setStatus(entity.getStatus());
        r.setAdminNotes(entity.getAdminNotes());
        r.setCreatedAt(entity.getCreatedAt());
        r.setUpdatedAt(entity.getUpdatedAt());
        r.setRepliedAt(entity.getRepliedAt());
        return r;
    }

    public List<ContactMessageResponse> toResponseList(List<ContactMessage> list) {
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }
}


