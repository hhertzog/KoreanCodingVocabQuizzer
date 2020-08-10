package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.*;

public class MongoDBVocabManager {
    private MongoCollection<Vocab> mongoCollection;

    @Autowired
    public MongoDBVocabManager(@NonNull MongoCollection<Vocab> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public void loadMongoVocabsIntoMap(@NonNull PriorityVocabMap vocabMap) {
        for (Vocab vocab : getAllVocabsAsList()) {
            vocabMap.addVocab(vocab);
        }
    }

    public void updatePrioritiesInDatabase(Set<Vocab> vocabSet) {
        for (Vocab vocab : vocabSet) {
            mongoCollection.updateOne(makeFilterUpdateQuery(vocab), makeUpdatedPriorityDoc(vocab));
        }
    }

    private Bson makeFilterUpdateQuery(Vocab vocab) {
        return and(eq("engWord", vocab.getEngWord()), eq("korWord", vocab.getKorWord()));
    }

    private Document makeUpdatedPriorityDoc(Vocab vocab) {
        return new Document("$set", new Document("priority", vocab.getPriority()));
    }

    private List<Vocab> getAllVocabsAsList() {
        return mongoCollection
                .find()
                .into(new ArrayList<>());
    }
}
