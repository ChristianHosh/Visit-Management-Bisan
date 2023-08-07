package com.example.vm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@Table(name = "contact_model")
@EqualsAndHashCode(callSuper = true)
public class Contact extends ModelAuditSuperclass {


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;
    public static boolean isNotValidName(String name) {
        return !name.matches("[a-zA-Z ]+");
    }
    public static boolean isNotValidLength(String string) {
        return string.length() > 30;
    }
    public static boolean isNotValidEmailLength(String string) {
        return string.length() > 50;
    }

   /* public static boolean isNotValidLength(String string) {
        return string.length() > 30;
    }
    public static boolean isNotValidEmailLength(String string) {
        return string.length() > 50;
    }
    public static boolean isNotValidLength(String string) {
        return string.length() > 30;
    }*/
}
