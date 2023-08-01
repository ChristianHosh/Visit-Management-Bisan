package com.example.vm.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(nullable = false)
    private int accessLevel;

}
