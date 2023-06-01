package com.dhbw.unternehmenb.ssp.interfaces;

import com.dhbw.unternehmenb.ssp.model.*;
import com.dhbw.unternehmenb.ssp.model.response.AllUsersVEnvRequestResponseBody;
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

    @DeleteMapping("vacation_request")
    @Operation(summary = "Delete Vacation Request by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vacation Request deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Vacation Request not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete Vacation Request", content = @Content),
    })
    @Tag(name = "VacationRequests")
    ResponseEntity<String> deleteVacationRequest(@RequestParam String vacationRequestId) throws Exception;

    @GetMapping("/v_environment_request")
    @Operation(summary = "Get all virtual environment requests from the logged in user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Virtual environment requests sent", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VirtualEnvironmentRequest.class))),
    })
    @Tag(name = "VirtualEnvironmentRequests")
    ResponseEntity<List<VirtualEnvironmentRequest>> getVirtualEnvironmentRequestsFromUser() throws Exception;

    @GetMapping("/v_environment")
    @Operation(summary = "Get all virtual environments from the logged in user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Virtual environments sent", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VirtualEnvironment.class))),
    })
    @Tag(name = "VirtualEnvironments")
    ResponseEntity<List<VirtualEnvironment>> getVirtualEnvironmentsFromUser() throws Exception;


    @GetMapping("v_environment_request/all")
    @Operation(summary = "Get all virtual environment requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Virtual environment requests sent", content = @Content)
    }
    )
    @Tag(name = "VirtualEnvironmentRequests")
    ResponseEntity<List<AllUsersVEnvRequestResponseBody>> getAllVirtualEnvironmentRequests() throws Exception;

    @PostMapping("/v_environment_request")
    @Operation(summary = "Create Virtual Environment Request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Virtual Environment Request created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Virtual Environment Request not created", content = @Content)
    })
    @Tag(name = "VirtualEnvironmentRequests")
    ResponseEntity<String> createVirtualEnvironmentRequest(
            @RequestParam String environmentType,
            @RequestParam(required = false) String comment
    ) throws Exception;

    @PutMapping("/v_environment_request/status")
    @Operation(summary = "set Status of virtual environment requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VirtualEnvironment.class))),
            @ApiResponse(responseCode = "401", description = "can only be updated by MANAGER", content = @Content),
            @ApiResponse(responseCode = "403", description = "can not modify requested that is approved/rejected", content = @Content),
            @ApiResponse(responseCode = "404", description = "Request not found", content = @Content)
    })
    @Tag(name = "VirtualEnvironmentRequests")
    ResponseEntity<VirtualEnvironment> setEnvironmentStatus(
            @RequestParam String id,
            @RequestParam Status status,
            @RequestParam(required = false) String rejectReason
    ) throws Exception;

    @DeleteMapping("/v_environment_request")
    @Operation(summary = "Delete virtual environment Request by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "virtual environment request deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "virtual environment request not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Failed to delete virtual environment request", content = @Content),
    })
    @Tag(name = "VirtualEnvironmentRequests")
    ResponseEntity<String> deleteVirtualEnvironmentRequest(@RequestParam String vacationRequestId) throws Exception;

}
