package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface VacationRequestRepository extends MongoRepository<VacationRequest,String> {
    boolean existsByUserAndVacationStartBetweenOrVacationEndBetween(User user, Date vacationStart, Date vacationStart2, Date vacationEnd, Date vacationEnd2);
}
