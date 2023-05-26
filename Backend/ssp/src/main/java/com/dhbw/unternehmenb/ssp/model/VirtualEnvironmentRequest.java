package com.dhbw.unternehmenb.ssp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection = "VirtualEnvironmentRequest")
public class VirtualEnvironmentRequest {
    @Id
    private UUID VirtualEnvironmentRequestId;
    @JsonIgnore
    @DBRef
    private User user;
    private EnvironmentType environmentType;
    private String comment;
    private Status status;
    private String rejectReason;
}
