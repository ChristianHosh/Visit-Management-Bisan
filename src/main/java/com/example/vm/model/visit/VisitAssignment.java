package com.example.vm.model.visit;

import com.example.vm.model.Customer;
import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "visit_assignment_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitAssignment extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "enabled")
    private int enabled;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "definition_id")
    @JsonBackReference
    private VisitDefinition visitDefinition;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "customer_assignment_model",
            joinColumns = @JoinColumn(name = "assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers;

    public VisitAssignmentListPayload toListPayload() {
        return new VisitAssignmentListPayload(this.getUuid(), this.getDate(), this.getComment(), this.getEnabled());
    }

    public VisitAssignmentDetailPayload toDetailPayload() {
        return new VisitAssignmentDetailPayload(this.getCreatedTime(), this.getLastModifiedTime(), this.getUuid(),
                this.getDate(), this.getComment(), this.getEnabled(),
                this.getCustomers().stream().map(Customer::toListPayload).toList());
    }
}
