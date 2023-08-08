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
    private Integer  type;

    @Column(name = "frequency")
    private Integer  frequency;

    @Column(name = "allow_recurring")
    private Boolean allowRecurring;

}
