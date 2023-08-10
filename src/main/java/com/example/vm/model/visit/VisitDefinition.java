package com.example.vm.model.visit;


import com.example.vm.model.Customer;
import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.payload.list.VisitDefinitionListPayload;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "visit_definition_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitDefinition extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private int type;

    @Column(name = "frequency")
    private int frequency;

    @Column(name = "allow_recurring")
    private boolean allowRecurring;

    @Column(name = "enabled", length = 1, nullable = false)
    private int enabled;

    @OneToMany(mappedBy = "visitDefinition", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VisitAssignment> visitAssignments;

    public VisitDefinitionListPayload toListPayload() {
        return new VisitDefinitionListPayload(this.getUuid(), this.getName(), this.getDescription(),
                this.getType(), this.getFrequency(), this.isAllowRecurring(), this.getEnabled());
    }

    public VisitDefinitionDetailPayload toDetailPayload() {
        return new VisitDefinitionDetailPayload(this.getCreatedTime(), this.getLastModifiedTime(), this.getUuid(),
                this.getName(), this.getDescription(),
                this.getType(), this.getFrequency(), this.isAllowRecurring(), this.getEnabled(),

                this.getVisitAssignments().stream().map(VisitAssignment::toListPayload).toList());
    }
}

