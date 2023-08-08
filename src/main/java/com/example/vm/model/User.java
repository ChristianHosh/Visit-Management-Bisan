package com.example.vm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Builder
@Table(name = "user_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends ModelAuditSuperclass {

    @Id
    @Column(name = "username", nullable = false, updatable = false, length = 30)
    private String username;

    @JsonIgnore
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


    public static boolean isNotValidAccessLevel(int accessLevel) {
        return (accessLevel != 1) && (accessLevel != 0);
    }

}
