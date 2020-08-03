package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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

    public void loadMongoVocabsIntoMap(@NonNull PriorityVocabMap vocabMap) {
        MongoCollection<Vocab> vocabCollection = getVocabCollection();
        loadAllVocabs(vocabCollection, vocabMap);
    }

    private void loadAllVocabs(MongoCollection<Vocab> vocabCollection, PriorityVocabMap vocabMap) {
        MongoCursor<Vocab> cursor = vocabCollection.find().iterator();
        while (cursor.hasNext()) {
            Vocab currentVocab = cursor.next();
            vocabMap.addVocab(currentVocab.getPriority(), currentVocab);
        }
        cursor.close();
    }

    private MongoCollection<Vocab> getVocabCollection() {
        return mongoClient.getDatabase(databaseName).getCollection(collectionName, Vocab.class);
    }
}
