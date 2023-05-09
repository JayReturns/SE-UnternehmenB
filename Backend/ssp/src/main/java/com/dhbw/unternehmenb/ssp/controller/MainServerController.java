package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MainServerController implements ServerApi {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FirebaseAuth firebaseAuth;

    private FirebaseToken getFirebaseToken(String token) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(token.substring(7));
    }

    @Override
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String auth) throws Exception {
        FirebaseToken decodedToken = getFirebaseToken(auth);
        Optional<User> userData = userRepository.findById(decodedToken.getUid());
        if (userData.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createUser(
            String auth,
            String name,
            String lastname,
            Role role,
            int vacationDays
    ) throws Exception {
        FirebaseToken decodedToken = getFirebaseToken(auth);
        User user = new User(decodedToken, name, lastname, vacationDays, role);
        try {
            userRepository.save(user);
            return new ResponseEntity<>("Success!", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
