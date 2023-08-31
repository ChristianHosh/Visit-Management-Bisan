package com.example.vm.model;

import com.example.vm.payload.report.AssignmentReportListPayload;
import com.example.vm.payload.report.UserAssignmentReportPayload;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "visit_assignment_model")
@NoArgsConstructor
@AllArgsConstructor
public class VisitAssignment extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "definition_id")
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
    private VisitAssignment nextVisitAssignment;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "visitAssignment", cascade = CascadeType.ALL)
    private List<VisitForm> visitForms;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        VisitAssignment that = (VisitAssignment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "VisitAssignment{" +
                "id=" + id +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                '}';
    }
}
