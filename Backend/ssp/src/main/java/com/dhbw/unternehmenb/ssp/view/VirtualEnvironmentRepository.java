package com.dhbw.unternehmenb.ssp.view;

import com.dhbw.unternehmenb.ssp.model.VirtualEnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface VirtualEnvironmentRepository extends MongoRepository<VirtualEnvironment, UUID> {
}
