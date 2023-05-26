package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.Status;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VacationRequestRepository extends MongoRepository<VacationRequest, UUID> {
    @ExistsQuery(value = "{ " +
            "'user.$id' : ?0, " +
            "$or:[ {'vacationStart' : { $gte: ?1, $lte: ?2 }}, {'vacationEnd' : { $gte: ?1, $lte: ?2 }} ], " +
            "'status' : { $ne: 'REJECTED' }" +
        "}")
    boolean isOverlappingWithAnotherVacationRequest(
            String userId,
            LocalDate vacationStart,
            LocalDate vacationEnd
    );

    List<VacationRequest> findByUserOrderByVacationStartDesc(User user);

    List<VacationRequest> findByUserAndVacationStartAfterAndVacationEndBeforeAndAndStatusNot(User user, LocalDate lastDayOfYearBefore, LocalDate firstOfNextYear, Status status);

    List<VacationRequest> findAllByUser(User user);
  
    void deleteByVacationRequestId(UUID vacationRequestId);  

}
