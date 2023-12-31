package com.example.vm.model;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public class ModelAuditSuperclass implements Serializable {

    @Column(name = "created_time", nullable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Timestamp createdTime;

    @Column(name = "last_modified_time", nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private Timestamp lastModifiedTime;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

}

