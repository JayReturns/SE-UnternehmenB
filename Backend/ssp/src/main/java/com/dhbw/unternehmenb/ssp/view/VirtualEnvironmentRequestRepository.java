package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.VirtualEnvironmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VirtualEnvironmentRequestRepository extends MongoRepository<VirtualEnvironmentRequest,String> {
}
