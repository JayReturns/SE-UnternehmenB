package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.User;
import com.dhbw.unternehmenb.ssp.model.VirtualEnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface VirtualEnvironmentRepository extends MongoRepository<VirtualEnvironment, UUID> {

    List<VirtualEnvironment> findAllByUser (User user);
}
