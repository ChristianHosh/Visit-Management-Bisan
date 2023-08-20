package com.example.vm.model;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Builder
@Table(name = "City_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class City extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name", nullable = false)
    private String name;


}
