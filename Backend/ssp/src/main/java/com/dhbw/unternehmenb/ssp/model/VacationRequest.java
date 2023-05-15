package com.dhbw.unternehmenb.ssp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document(collection="VacationRequest")
public class VacationRequest {
    @Id
    private String vacationRequestId;
    @DBRef
    private User user;
    private LocalDateTime vacationStart;
    private LocalDateTime vacationEnd;
    private int vacationDays;
    private String comment;
    private Status status;
    private String rejectReason;
}
