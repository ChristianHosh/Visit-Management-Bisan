package com.example.vm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "user_model")
@EqualsAndHashCode(callSuper = true)
public class User extends ModelAuditSuperclass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_name")
    private String username;

    @Column(name = "Passwsord")
    private String password;

    @Column(name ="first_Name")
    private String firstName;

    @Column(name = "last_Name")
    private String lastname;



}
