package com.example.vm.model;

import com.example.vm.payload.CustomerPayload;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@Table(name = "customer_model")
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<Contact> contacts;

    public CustomerPayload toPayload(){
        return new CustomerPayload(this.uuid, this.name, this.enabled);
    }

}
