package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MongoDBVocabLoaderTests {
    private final int LOWEST_PRIORITY = 1;
    private final int HIGHEST_PRIORITY = 3;
    private final String DATABASE_NAME = "database_name";
    private final String COLLECTION_NAME = "collection_name";
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";

    private final Vocab VOCAB1 = new Vocab(LOWEST_PRIORITY, ENGLISH + 1, KOREAN + 1);
    private final Vocab VOCAB2 = new Vocab(LOWEST_PRIORITY, ENGLISH + 2, KOREAN + 2);
    private final Vocab VOCAB3 = new Vocab(LOWEST_PRIORITY, ENGLISH + 3, KOREAN + 3);
    private PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);
    private ArrayList<Vocab> vocabList = new ArrayList<>();

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Vocab> mongoCollection;

    @Mock
    private FindIterable<Vocab> mongoFind;

    @InjectMocks
    private MongoDBVocabLoader loader = new MongoDBVocabLoader(mongoClient, DATABASE_NAME, COLLECTION_NAME);

    @BeforeEach
    public void runBefore() {
        when(mongoClient.getDatabase(DATABASE_NAME)).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(COLLECTION_NAME, Vocab.class)).thenReturn(mongoCollection);
        when(mongoCollection.find(new Document(), Vocab.class)).thenReturn(mongoFind);
        when(mongoFind.into(any())).thenReturn(vocabList);
    }

    @Test
    public void whenLoadMongoVocabsIntoMap_givenVocabsPresentInDB_thenLoadsVocabsIntoMap() {
        vocabList.add(VOCAB1);
        vocabList.add(VOCAB2);
        vocabList.add(VOCAB3);

        loader.loadMongoVocabsIntoMap(map);

        assertThat(map.size() == 3);
        assertThat(map.get(LOWEST_PRIORITY).contains(VOCAB1));
        assertThat(map.get(LOWEST_PRIORITY).contains(VOCAB2));
        assertThat(map.get(LOWEST_PRIORITY).contains(VOCAB3));
    }

    @Test
    public void whenLoadMongoVocabsIntoMap_givenNoVocabsInDB_thenMapStaysEmpty() {
        loader.loadMongoVocabsIntoMap(map);

        assertThat(map.size() == 0);
    }

    @AfterEach
    public void runAfter() {
        map.clear();
        vocabList.clear();
    }
}
