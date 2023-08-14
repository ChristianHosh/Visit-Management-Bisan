package com.example.vm.model;

import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.payload.detail.CustomerDetailPayload;
import com.example.vm.payload.list.CustomerListPayload;
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

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "customer_assignment_model",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "assignment_id")
    )
    private List<VisitAssignment> visitAssignments;

    public CustomerListPayload toListPayload(){
        return new CustomerListPayload(this.getUuid(), this.getName(), this.getEnabled());
    }

    public CustomerDetailPayload toDetailPayload(){
        return new CustomerDetailPayload(this.getCreatedTime(), this.getLastModifiedTime(), this.getUuid(),
                this.getName(), this.getEnabled(), this.getAddress(), this.getContacts(),
                this.getVisitAssignments().stream().map(VisitAssignment::toListPayload).toList());
    }


}
