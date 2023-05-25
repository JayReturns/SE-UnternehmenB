package com.dhbw.unternehmenb.ssp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class LeftAndMaxVacationDays {
    private int maxDays;
    private AtomicInteger leftDays;

}
