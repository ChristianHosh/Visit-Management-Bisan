package com.example.vm.dto.mapper;

import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.response.ContactResponse;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.VisitType;
import com.example.vm.service.formatter.PhoneNumberFormatter;

import java.util.List;

public class ContactMapper {

    public static ContactResponse toListResponse(Contact contact) {
        if (contact == null) return null;

        ContactResponse response = new ContactResponse();

        response.setId(contact.getId());
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

    public static Contact toEntity(ContactRequest contactRequest, Customer customer, List<VisitType> visitTypes) {
        return Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .email(contactRequest.getEmail())
                .phoneNumber(PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber()))
                .visitTypes(visitTypes)
                .customer(customer)
                .build();
    }

    public static void update(Contact contactToUpdate, ContactRequest contactRequest, List<VisitType> visitTypes) {
        contactToUpdate.setFirstName(contactRequest.getFirstName());
        contactToUpdate.setLastName(contactRequest.getLastName());
        contactToUpdate.setPhoneNumber(PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber()));
        contactToUpdate.setEmail(contactRequest.getEmail());
        contactToUpdate.setVisitTypes(visitTypes);
    }
}
