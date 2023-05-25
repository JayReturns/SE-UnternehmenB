package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VacationRequestRepository extends MongoRepository<VacationRequest, UUID> {
    boolean existsByUserAndVacationStartBetweenOrVacationEndBetween(
            User user,
            LocalDate vacationStart,
            LocalDate vacationStart2,
            LocalDate vacationEnd,
            LocalDate vacationEnd2
    );

    boolean existsByUserAndVacationStartIsOrVacationEndIs(User user, LocalDate vacationStart, LocalDate vacationEnd);

    List<VacationRequest> findByUserOrderByVacationStartDesc(User user);

    List<VacationRequest> findByUserAndVacationStartAfterAndVacationEndBefore(User user, LocalDate lastDayOfYearBefore, LocalDate firstOfNextYear);

    List<VacationRequest> findAllByUser(User user);
  
    void deleteByVacationRequestId(UUID vacationRequestId);  

}
