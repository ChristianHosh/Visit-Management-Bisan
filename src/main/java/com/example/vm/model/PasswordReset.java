package com.example.vm.model;

import com.example.vm.model.enums.PasswordStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "password_reset_model")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordReset extends ModelAuditSuperclass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Enumerated
    @Column(name = "status", nullable = false)
    private PasswordStatus status = PasswordStatus.PENDING;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "user_username", nullable = false)
    private User user;

}