package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.models.User;
import com.dhbw.unternehmenb.ssp.repositories.Authenticater;
import com.dhbw.unternehmenb.ssp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testing {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Authenticater authenticater;

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setBenutzerID("rkPe8ykWFqgYJnGuVfGmMhqbF1r2");
        user.setNachname("test");
        user.setVorname("test");
        user.setUrlaubstage(0);
        user.setRolle("test");

        try{
            userRepository.insert(user);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/authtest")
    public ResponseEntity<String> authtest(
            @RequestHeader("Authorization") String auth
    ){

        if(authenticater.authenticate(auth)){
            return ResponseEntity.ok("Success");
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
