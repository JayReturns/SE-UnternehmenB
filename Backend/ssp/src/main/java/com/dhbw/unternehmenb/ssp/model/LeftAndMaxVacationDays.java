package com.dhbw.unternehmenb.ssp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeftAndMaxVacationDays {
    private int maxDays;
    private int leftDays;
    private int leftDaysWithoutRequestedDays;

}
