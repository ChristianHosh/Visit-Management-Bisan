package com.example.vm.model.visit;


import com.example.vm.model.ModelAuditSuperclass;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.payload.list.VisitDefinitionListPayload;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "visit_definition_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitDefinition extends ModelAuditSuperclass {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "type_id")
    private VisitType type;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @Column(name = "allow_recurring", nullable = false)
    private boolean allowRecurring;

    @OneToMany(mappedBy = "visitDefinition", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VisitAssignment> visitAssignments;

    public VisitDefinitionListPayload toListPayload() {
        return new VisitDefinitionListPayload(this.getId(), this.getName(), this.getDescription(),
                this.getType(), this.getFrequency(), this.isAllowRecurring(), this.getEnabled());
    }

    public VisitDefinitionDetailPayload toDetailPayload() {
        return new VisitDefinitionDetailPayload(this.getCreatedTime(), this.getLastModifiedTime(), this.getId(),
                this.getName(), this.getDescription(),
                this.getType(), this.getFrequency(), this.isAllowRecurring(), this.getEnabled(),
                this.getVisitAssignments().stream().map(VisitAssignment::toListPayload).toList());
    }
}

