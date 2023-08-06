package com.example.vm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "user_model")
@EqualsAndHashCode(callSuper = true)
public class User extends ModelAuditSuperclass {

}
