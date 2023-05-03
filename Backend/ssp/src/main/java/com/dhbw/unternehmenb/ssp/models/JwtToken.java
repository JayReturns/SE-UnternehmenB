package com.dhbw.unternehmenb.ssp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtToken {
    @JsonProperty("auth_time")
    private Date authTime;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty
    private String email;

    public String getUserId() {
        return userId;
    }
}