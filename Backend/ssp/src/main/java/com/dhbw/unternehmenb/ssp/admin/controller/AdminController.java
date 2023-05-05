package com.dhbw.unternehmenb.ssp.admin.controller;

import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping(path = "/hello")
    public ResponseEntity<String> setUserClaim() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
