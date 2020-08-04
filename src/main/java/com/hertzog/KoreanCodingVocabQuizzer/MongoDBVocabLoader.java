package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

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

    public void loadMongoVocabsIntoMap(@NonNull PriorityVocabMap vocabMap) {
        for (Vocab vocab : getAllVocabsAsList()) {
            vocabMap.addVocab(vocab.getPriority(), vocab);
        }
    }

    public List<Vocab> getAllVocabsAsList() {
        return getVocabCollection().find(new Document(), Vocab.class).into(new ArrayList<>());
    }

    private MongoCollection<Vocab> getVocabCollection() {
        return mongoClient.getDatabase(databaseName).getCollection(collectionName, Vocab.class);
    }
}
