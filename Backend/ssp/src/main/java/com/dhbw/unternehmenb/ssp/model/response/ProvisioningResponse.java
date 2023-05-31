package com.dhbw.unternehmenb.ssp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProvisioningResponse {
    private String id;
    private String type;
    private Boolean verificationSuccessful;
    private String ipAddress;
    private String userName;
    private String initialPassword;
}
