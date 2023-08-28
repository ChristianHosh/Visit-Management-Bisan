package com.example.vm.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ModelAuditResponse {

    private Timestamp createdTime;
    private Timestamp lastModifiedTime;
    private Boolean enabled;

}
