package com.feynman.orch.repository;

import com.feynman.orch.model.OrchRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrchRepository extends MongoRepository<OrchRecord, String> {
}
