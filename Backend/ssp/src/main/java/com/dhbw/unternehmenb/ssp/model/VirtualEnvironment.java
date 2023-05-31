package com.dhbw.unternehmenb.ssp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection = "VirtualEnvironment")
public class VirtualEnvironment {
    @Id
    private UUID VirtualEnvironmentId;
    @DBRef
    private User user;
    private String environmentType;
    private String ipAddress;
    private String userName;
    private String password;
}
