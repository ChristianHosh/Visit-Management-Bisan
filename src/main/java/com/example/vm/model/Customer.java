package com.example.vm.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;


@Data
@Entity
@Table(name = "customer_model")
@EqualsAndHashCode(callSuper = true)
public class Customer extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "enabled", length = 1, nullable = false)
    private int enabled;

    //ADD ADDRESS COLUMN OBJECT

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<Contact> contacts;

    public static boolean isNotValidLength(String string) {
        return string.length() > 30;
    }

}
