package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.EnvironmentType;
import com.dhbw.unternehmenb.ssp.model.Status;
import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VirtualEnvironmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface VirtualEnvironmentRequestRepository extends MongoRepository<VirtualEnvironmentRequest, UUID> {

    boolean existsByUserAndEnvironmentTypeAndStatus(User user, EnvironmentType environmentType, Status status);

}
