package com.example.vm.model.visit;

import com.example.vm.model.Customer;
import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.payload.list.VisitDefinitionListPayload;
import com.example.vm.payload.list.VisitFormListPayload;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "visit_assignment_id")
    private VisitAssignment visitAssignment;

    public VisitFormListPayload toListPayload() {
      return new VisitFormListPayload(
              this.getUuid(),this.getStartTime(),this.getEndTime(),this.getStatus(),this.getNote(),
              this.getLongitude(),this.getLatitude(), this.getCustomer().toListPayload(),this.getVisitAssignment().toListPayload());
    }
}
