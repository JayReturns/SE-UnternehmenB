package com.dhbw.unternehmenb.ssp.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1")
public interface ServerApi {
    @GetMapping("user")
    @Operation(summary = "User daten abfragen")
    ResponseEntity<String> getUserData() throws Exception;
    //TODO: we may want to use Response/Request-bodies for more data
}
