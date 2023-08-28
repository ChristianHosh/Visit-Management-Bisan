package com.example.vm.dto.mapper;

import com.example.vm.dto.response.ContactResponse;
import com.example.vm.model.Contact;

import java.util.List;

public class ContactMapper {

    public static ContactResponse toListResponse(Contact contact) {
        if (contact == null) return null;

        ContactResponse response = new ContactResponse();

        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setVisitTypes(VisitTypeMapper.listToResponseList(contact.getVisitTypes()));

        response.setEnabled(contact.getEnabled());
        response.setCreatedTime(contact.getCreatedTime());
        response.setLastModifiedTime(contact.getLastModifiedTime());

        return response;
    }

    public static List<ContactResponse> listToResponseList(List<Contact> contactList) {
        if (contactList == null) return null;

        return contactList
                .stream()
                .map(ContactMapper::toListResponse)
                .toList();
    }
}
