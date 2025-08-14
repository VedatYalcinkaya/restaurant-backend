package com.demirciyazilim.business.abstracts;

import com.demirciyazilim.business.dtos.contactmessage.requests.CreateContactMessageRequest;
import com.demirciyazilim.business.dtos.contactmessage.responses.ContactMessageResponse;
import com.demirciyazilim.core.utilities.results.DataResult;
import com.demirciyazilim.core.utilities.results.Result;
import com.demirciyazilim.entities.enums.ContactMessageStatus;

import java.util.List;

public interface ContactMessageService {
    DataResult<ContactMessageResponse> createAndNotify(CreateContactMessageRequest request);
    DataResult<List<ContactMessageResponse>> getAll(int page, int size);
    DataResult<List<ContactMessageResponse>> getByStatus(ContactMessageStatus status, int page, int size);
    Result markRead(Long id);
    Result reply(Long id, String adminNotes);
    Result delete(Long id);
}


