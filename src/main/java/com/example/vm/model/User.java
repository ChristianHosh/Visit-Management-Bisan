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
    @Column(name = "username", nullable = false, updatable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 30)
    private String password;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "access_level", length = 1, nullable = false)
    private int accessLevel;

    @Column(name = "enabled", length = 1, nullable = false)
    private int enabled;


}
