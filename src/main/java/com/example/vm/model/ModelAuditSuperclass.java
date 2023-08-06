package com.example.vm.model;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class ModelAuditSuperclass implements Serializable {

    @Column(name = "created_time", nullable = false, updatable = false)
    private Timestamp createdTime;

    @Column(name = "last_modified_time", nullable = false)
    private Timestamp lastModifiedTime;

}

