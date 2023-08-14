package com.example.vm.payload.list;

import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor

public class ContactListPayload {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private int enabled;

    private List<VisitType> visitTypes;

    public static List<ContactListPayload> toPayload(List<Contact> contactList) {
        return contactList.stream().map(Contact::toListPayload).toList();
    }

}
