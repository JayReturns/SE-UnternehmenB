package com.dhbw.unternehmenb.ssp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="User")
public class User {

    @Id
    private String benutzerID;
    private String email;
    private String vorname;
    private String nachname;
    private int Urlaubstage;
    private String rolle;

    public String getBenutzerID() {
        return benutzerID;
    }

    public void setBenutzerID(String benutzerID) {
        this.benutzerID = benutzerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public int getUrlaubstage() {
        return Urlaubstage;
    }

    public void setUrlaubstage(int urlaubstage) {
        Urlaubstage = urlaubstage;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    @Override
    public String toString() {
        return "User{" +
                "benutzerID='" + benutzerID + '\'' +
                ", email='" + email + '\'' +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", Urlaubstage=" + Urlaubstage +
                ", rolle='" + rolle + '\'' +
                '}';
    }
}
