package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

import java.util.List;

public interface VacationRequestRepository extends MongoRepository<VacationRequest,String> {
    boolean existsByUserAndVacationStartBetweenOrVacationEndBetween(User user, LocalDateTime vacationStart, LocalDateTime vacationStart2, LocalDateTime vacationEnd, LocalDateTime vacationEnd2);

    List<VacationRequest> findByUserOrderByVacationStartDesc(User user);

}
