package com.example.vm.payload.list;

import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class ContactListPayload {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private boolean enabled;

    private List<VisitType> visitTypes;

    public static List<ContactListPayload> toPayload(List<Contact> contactList) {
        return contactList.stream().map(Contact::toListPayload).toList();
    }

}
