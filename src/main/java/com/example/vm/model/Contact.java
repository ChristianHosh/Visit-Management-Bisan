package com.example.vm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.util.regex.Pattern;

@Data
@Entity
@Builder
@Table(name = "contact_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contact extends ModelAuditSuperclass {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "";

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

    @Column(name = "enabled", length = 1, nullable = false)
    private int enabled;

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

    public static boolean isNotValidEmail(String email) {
        return !Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches() || email.length() > 50;
    }

    public static boolean isNotValidNumber(String phoneNumber){
        return !Pattern.compile(PHONE_REGEX)
                .matcher(phoneNumber)
                .matches();
    }
}
