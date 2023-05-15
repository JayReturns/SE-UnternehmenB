package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VacationRequestRepository extends MongoRepository<VacationRequest,String> {

    List<VacationRequest> findByUserOrderByVacationStartDesc(User user);

}
