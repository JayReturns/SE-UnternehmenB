package com.dhbw.unternehmenb.ssp.model.dto;

import com.dhbw.unternehmenb.ssp.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationRequestDTO {
    private String vacationRequestId;
    private Date vacationStart;
    private Date vacationEnd;
    private int vacationDays;
    private String comment;
    private Status status;
    private String rejectReason;
}
