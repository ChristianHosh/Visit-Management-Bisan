package com.example.vm.model.visit;

import com.example.vm.model.ModelAuditSuperclass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
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
}
