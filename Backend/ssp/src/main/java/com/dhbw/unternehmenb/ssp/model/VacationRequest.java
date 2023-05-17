package com.dhbw.unternehmenb.ssp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection="VacationRequest")
public class VacationRequest {
    @Id
    private UUID vacationRequestId;
    @JsonIgnore
    @DBRef
    private User user;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private int duration;
    private String comment;
    private Status status;
    private String rejectReason;
}
