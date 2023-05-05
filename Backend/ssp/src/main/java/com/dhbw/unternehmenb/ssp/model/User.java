package com.dhbw.unternehmenb.ssp.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private String firstname;
    private String lastname;

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
