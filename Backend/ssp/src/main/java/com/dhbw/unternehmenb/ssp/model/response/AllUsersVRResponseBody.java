package com.dhbw.unternehmenb.ssp.model.response;

import com.dhbw.unternehmenb.ssp.model.LeftAndMaxVacationDays;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllUsersVRResponseBody {

    private UserWithLeftDaysDTO user;

    private List<VacationRequest> requests;

}
