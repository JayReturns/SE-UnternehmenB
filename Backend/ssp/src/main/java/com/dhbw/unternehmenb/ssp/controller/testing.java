/*package com.dhbw.unternehmenb.ssp.controller;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.view.UserRepository;
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

    @GetMapping("/test")
    public ResponseEntity<String> test(){

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setUserId("rkPe8ykWFqgYJnGuVfGmMhqbF1r2");
        user.setLastName("test");
        user.setName("test");
        user.setVacationDays(0);
        user.setRole("test");

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
            return new ResponseEntity(HttpStatus.OK);

    }
}*/
