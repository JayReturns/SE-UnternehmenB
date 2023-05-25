package com.dhbw.unternehmenb.ssp.interfaces;

import com.dhbw.unternehmenb.ssp.model.*;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            @ApiResponse(responseCode = "200", description = "User sent", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @Tag(name = "User")
    ResponseEntity<User> getUserData() throws Exception;


    @PostMapping("user")
    @Operation(summary = "Register User")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
    })
    @Tag(name = "User")
    ResponseEntity<String> createUser(
            @RequestParam String name,
            @RequestParam String lastname,
            @RequestParam Role role,
            @RequestParam(defaultValue = "30") int vacationDays
    ) throws Exception;

    @PostMapping("vacation_request")
    @Operation(summary = "Create Vacation Request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vacation Request created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request (e.g. not enough Days available, Vacation request overlaps with another vacation, ...)", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Vacation Request not created", content = @Content),
    })
    @Tag(name = "VacationRequests")
    ResponseEntity<String> createVacationRequest(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int duration,
            @RequestParam(required = false) String comment
    ) throws Exception;

    @Operation(summary = "Get all vacation requests from the logged in user")
    @GetMapping("vacation_request")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vacation requests sent", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VacationRequest.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @Tag(name = "VacationRequests")
    ResponseEntity<List<VacationRequest>> getVacationRequestsFromUser() throws Exception;

    @GetMapping("/vacation_request/all")
    @Operation(summary = "Alle Urlaubsanträge für den Geschäftsleiter anfragen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daten erhalten", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AllUsersVRResponseBody.class)))),
            @ApiResponse(responseCode = "401", description = "User nicht angemeldet oder kein Geschäftsleiter", content = @Content)
    })
    @Tag(name = "VacationRequests")
    ResponseEntity<List<AllUsersVRResponseBody>> getAllVRs() throws Exception;

    @PutMapping("/vacation_request")
    @Operation(summary = "Urlaubsanträge bearbeiten")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "vacation request updated", content = @Content)
    })
    @Tag(name = "VacationRequests")
    ResponseEntity<String> putVacationRequest(
            @RequestParam String vacationId,
            @RequestParam(required = false) LocalDate begin,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String rejection_cause
    ) throws Exception;

    @Operation(summary = "Übrige und maximale Urlaubstage zurückgeben")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "calculated left days", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not Found")
    })
    @Tag(name = "DaysLeft")
    @GetMapping("/vacation/days")
    ResponseEntity<LeftAndMaxVacationDays> getLeftVacationDays(
            @RequestParam int year
    );
}
