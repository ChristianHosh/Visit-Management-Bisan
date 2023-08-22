package com.example.vm.model.visit;

import com.example.vm.model.Customer;
import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.model.User;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.payload.report.UserAssignmentReportPayload;
import com.example.vm.payload.report.AssignmentReportListPayload;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "visit_assignment_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitAssignment extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "enabled", nullable = false)
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

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "next_assignment_id")
    @ToString.Exclude
    @JsonBackReference
    private VisitAssignment nextVisitAssignment;

    public VisitAssignmentListPayload toListPayload() {
        return new VisitAssignmentListPayload(this.getId(), this.getDate(), this.getComment(), this.getEnabled());
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "username")
    @JsonManagedReference
    private User user;

    public VisitAssignmentDetailPayload toDetailPayload() {
        return new VisitAssignmentDetailPayload(this.getCreatedTime(), this.getLastModifiedTime(), this.getId(),
                this.getDate(), this.getComment(), this.getEnabled(),
                this.getCustomers().stream().map(Customer::toListPayload).toList(), this.getUser()
        );
    }

    public AssignmentReportListPayload toReportListPayload() {
        if (this.getUser() != null) {
            return new AssignmentReportListPayload(this.getId(), this.getComment(), this.getDate(),
                    this.getUser().getUsername(), this.getUser().getFirstName(),
                    this.getUser().getLastName(), this.getCustomers().stream().map(Customer::toListPayloadReport).toList()
            );
        } else {
            return new AssignmentReportListPayload(this.getId(), this.getComment(), this.getDate(),
                    "No User Assigned", "", "",
                    this.getCustomers().stream().map(Customer::toListPayloadReport).toList());
        }
    }

    public UserAssignmentReportPayload toListPayloadReportCustomer() {
        return new UserAssignmentReportPayload(this.getId(),
                this.getDate(),
                this.getUser().getUsername(),
                this.getUser().getFirstName(),
                this.getUser().getLastName(), this.getVisitDefinition().getType().getName()
        );

    }

}
