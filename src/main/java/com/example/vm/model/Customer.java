package com.example.vm.model;

import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.payload.detail.CustomerDetailPayload;
import com.example.vm.payload.list.AddressListPayload;
import com.example.vm.payload.list.CustomerListPayload;
import com.example.vm.payload.report.CustomerReportListPayload;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Entity
@Builder
@Table(name = "customer_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
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

    public CustomerListPayload toListPayload() {
        return new CustomerListPayload(
                this.getId(),
                this.getName(),
                this.getEnabled(),
                new AddressListPayload(
                        this.getAddress().getCity().getId(),
                        this.getAddress().getAddressLine1(),
                        this.getAddress().getAddressLine2(),
                        this.getAddress().getZipcode()));
    }

    public CustomerDetailPayload toDetailPayload() {
        return new CustomerDetailPayload(
                this.getCreatedTime(),
                this.getLastModifiedTime(),
                this.getId(),
                this.getName(),
                this.getEnabled(),
                this.getAddress(),
                this.getContacts(),
                this.getVisitAssignments().stream().map(VisitAssignment::toListPayload).toList());
    }

    public CustomerReportListPayload toListPayloadReport() {
        return new CustomerReportListPayload(this.getId(), this.getName());
    }


}
