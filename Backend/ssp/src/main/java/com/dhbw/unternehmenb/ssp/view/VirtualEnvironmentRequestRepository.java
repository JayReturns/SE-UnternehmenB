package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.VirtualEnvironmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface VirtualEnvironmentRequestRepository extends MongoRepository<VirtualEnvironmentRequest, UUID> {
}
