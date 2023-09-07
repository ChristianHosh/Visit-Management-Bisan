package com.example.vm.model;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.FormReportListPayload;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Entity
@Builder
@Table(name = "visit_form_model")
@NoArgsConstructor
@AllArgsConstructor
public class VisitForm extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Enumerated()
    @Column(name = "status", nullable = false)
    private VisitStatus status = VisitStatus.NOT_STARTED;

    @Column(name = "note")
    private String note;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "geo_coordinates_id", unique = true)
    private GeoCoordinates geoCoordinates;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "visit_assignment_id")
    private VisitAssignment visitAssignment;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "form_contact_model",
            joinColumns = @JoinColumn(name = "form_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Contact> contacts;

    public FormReportListPayload toListPayloadReport() {
        return new FormReportListPayload(
                this.getId(), this.getStatus(), this.getStartTime(), this.getEndTime(),
                this.getCustomer().getName(),
                this.getCustomer().getLocation().getAddress(),
                this.getVisitAssignment().getDate(),
                this.getVisitAssignment().getVisitDefinition().getType().getName()
        );
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        VisitForm visitForm = (VisitForm) o;
        return getId() != null && Objects.equals(getId(), visitForm.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "VisitForm{" +
                "id=" + id +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", customer=" + customer +
                '}';
    }
}
