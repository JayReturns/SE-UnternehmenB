package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.interfaces.ServerApi;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainServerController implements ServerApi {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<String> getUserData() throws Exception {
        String userData = userRepository.getUser();
        return new ResponseEntity<>("user data", HttpStatus.OK);
    }
}
