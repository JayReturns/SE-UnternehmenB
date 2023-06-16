package com.dhbw.unternehmenb.ssp.model.response;

import com.dhbw.unternehmenb.ssp.model.LeftAndMaxVacationDays;
import com.dhbw.unternehmenb.ssp.model.Role;
import com.dhbw.unternehmenb.ssp.model.User;
import lombok.Data;

@Data
public class UserWithLeftDaysDTO {
    private String userId;
    private String email;
    private String name;
    private String lastName;
    private LeftAndMaxVacationDays vacationDays;
    private Role role;

    public UserWithLeftDaysDTO( User user, LeftAndMaxVacationDays leftAndMaxVacationDays){
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.vacationDays = leftAndMaxVacationDays;
        this.role = user.getRole();
    }
}
