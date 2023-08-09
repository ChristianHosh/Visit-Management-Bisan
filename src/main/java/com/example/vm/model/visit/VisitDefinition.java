package com.example.vm.model.visit;


import com.example.vm.model.ModelAuditSuperclass;
import jakarta.persistence.*;
import lombok.*;

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
    private String description ;

    @Column(name = "type")
    private int  type;

    @Column(name = "frequency")
    private int  frequency;

    @Column(name = "allow_recurring")
    private boolean allowRecurring;

    @Column(name = "enabled", length = 1, nullable = false)
    private int enabled;

}
