package com.dhbw.unternehmenb.ssp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collation = "VirtualEnvironmentRequest")
public class VirtualEnvironmentRequest {
    @Id
    private UUID VirtualEnvironmentRequestId;
    @DBRef
    private User user;
    private EnvironmentType environmentType;
    private String comment;
    private Status status;
    private String rejectReason;
}
