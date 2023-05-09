package com.dhbw.unternehmenb.ssp.interfaces;

import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1")
public interface ServerApi {
    @GetMapping("user")
    @Operation(summary = "Get User Data")
    ResponseEntity<User> getUserData(@RequestHeader("Authorization") String auth) throws Exception;
    //we may want to use Response/Request-bodies for more data


    @PostMapping("user")
    @Operation(summary = "Get User Data")
    ResponseEntity<String> createUser(
            @RequestHeader("Authorization") String auth,
            @RequestParam String name,
            @RequestParam String lastname,
            @RequestParam Role role,
            @RequestParam(defaultValue = "30") int vacationDays
    ) throws Exception;



}
