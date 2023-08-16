package com.example.vm.model.visit;

import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.detail.VisitFormDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.VisitFormListPayload;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@Table(name = "visit_form_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitForm extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private VisitStatus status;

    @Column(name = "note")
    private String note;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "visit_assignment_id")
    private VisitAssignment visitAssignment;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "form_contact_model",
            joinColumns = @JoinColumn(name = "form_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Contact> contacts;


    public VisitFormDetailPayload toDetailPayload() {
        return new VisitFormDetailPayload(
                this.getUuid(), this.getStartTime(), this.getEndTime(), this.getStatus(), this.getNote(),
                this.getCustomer().toListPayload(),
                this.getVisitAssignment().toListPayload(),
                ContactListPayload.toPayload(this.getContacts()));
    }
    public VisitFormListPayload toListPayload(){
        return new VisitFormListPayload(this.getUuid(),
                this.getStatus(),
                this.getCustomer().toListPayload()
        );
    }
}
