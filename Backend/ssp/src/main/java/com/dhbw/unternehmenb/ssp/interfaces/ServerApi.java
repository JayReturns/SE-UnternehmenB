package com.dhbw.unternehmenb.ssp.interfaces;

import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVRResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SecurityRequirement(name = "Bearer Authentication")
public interface ServerApi {
    @GetMapping("user")
    @Operation(summary = "Get User Data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daten gesendet", content = @Content(mediaType = "application/json",schema = @Schema(implementation = User.class)))
    })
    ResponseEntity<User> getUserData() throws Exception;
    //we may want to use Response/Request-bodies for more data


    @PostMapping("user")
    @Operation(summary = "Register User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daten gesendet", content = @Content)
    })
    ResponseEntity<String> createUser(
            @RequestParam String name,
            @RequestParam String lastname,
            @RequestParam Role role,
            @RequestParam(defaultValue = "30") int vacationDays
    ) throws Exception;

    @GetMapping("/vacation_request/all")
    @Operation(summary = "Alle Urlaubsanträge für den Geschäftsleiter anfragen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daten erhalten", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AllUsersVRResponseBody.class))))
    })
    ResponseEntity<List<AllUsersVRResponseBody>> getAllVRs() throws Exception;

}
