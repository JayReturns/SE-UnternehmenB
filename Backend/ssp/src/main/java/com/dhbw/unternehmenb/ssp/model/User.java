package com.dhbw.unternehmenb.ssp.model;

import com.google.firebase.auth.FirebaseToken;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="User")
public class User {

    @Id
    private String userId;
    private String email;
    private String name;
    private String lastName;
    private int vacationDays;
    private Role role;

    @PersistenceCreator
    public User(String userId, String email, String name, String lastName, int vacationDays, Role role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.vacationDays = vacationDays;
        this.role = role;
    }

    public User(FirebaseToken idToken, String name, String lastName, int vacationDays, Role role) {
        this.userId = idToken.getUid();
        this.email = idToken.getEmail();
        this.name = name;
        this.lastName = lastName;
        this.vacationDays = vacationDays;
        this.role = role;
    }

}
