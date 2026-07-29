package com.feynman.orch.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBClean {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void clearDatabase() {
        mongoTemplate.getDb().drop();
        System.out.println("--> MongoDB database dropped and reset for fresh start!");
    }
}