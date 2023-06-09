package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VirtualEnvironmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface VirtualEnvironmentRequestRepository extends MongoRepository<VirtualEnvironmentRequest, UUID> {

    List<VirtualEnvironmentRequest> findAllByUser(User user);

    void deleteByVirtualEnvironmentRequestId(UUID virtualEnvironmentRequestId);
}
