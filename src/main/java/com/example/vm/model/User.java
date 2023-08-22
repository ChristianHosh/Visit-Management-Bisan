package com.example.vm.model;

import com.example.vm.model.visit.VisitAssignment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
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

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "access_level", nullable = false, length = 1)
    private int accessLevel;

    @Column(name = "enabled", nullable = false, length = 1)
    private int enabled;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    @JsonBackReference
    private List<VisitAssignment> visitAssignments;
}
