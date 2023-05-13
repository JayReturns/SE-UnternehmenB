package com.dhbw.unternehmenb.ssp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection="VacationRequest")
public class VacationRequest {
    @Id
    private String vacationRequestId;
    @JsonIgnore
    @DBRef
    private User user;
    private Date vacationStart;
    private Date VacationEnd;
    private int vacationDays;
    private String comment;
    private Status status;
    private String rejectReason;
}
