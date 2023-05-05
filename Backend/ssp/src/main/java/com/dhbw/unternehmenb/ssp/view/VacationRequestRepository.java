package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.VacationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VacationRequestRepository extends MongoRepository<VacationRequest,String> {
}
