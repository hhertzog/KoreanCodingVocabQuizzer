package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

public class MongoDBVocabLoader {
    private MongoClient mongoClient;
    private String databaseName;
    private String collectionName;

    @Autowired
    public MongoDBVocabLoader(@NonNull MongoClient mongoClient,
                              @NonNull String databaseName,
                              @NonNull String collectionName) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }
}
