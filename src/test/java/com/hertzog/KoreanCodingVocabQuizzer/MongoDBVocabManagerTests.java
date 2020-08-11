package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class MongoDBVocabManagerTests {
    private final int LOWEST_PRIORITY = 1;
    private final int HIGHEST_PRIORITY = 3;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";

    private final Vocab VOCAB1 = new Vocab(LOWEST_PRIORITY, ENGLISH + 1, KOREAN + 1);
    private final Vocab VOCAB2 = new Vocab(LOWEST_PRIORITY, ENGLISH + 2, KOREAN + 2);
    private final Vocab VOCAB3 = new Vocab(LOWEST_PRIORITY, ENGLISH + 3, KOREAN + 3);

    private final Document LOWEST_PRIORITY_SETTER = new Document("$set", new Document("priority", LOWEST_PRIORITY));
    private final Bson VOCAB1_FILTER = and(eq("engWord", ENGLISH + 1), eq("korWord", KOREAN + 1));
    private final Bson VOCAB2_FILTER = and(eq("engWord", ENGLISH + 2), eq("korWord", KOREAN + 2));
    private final Bson VOCAB3_FILTER = and(eq("engWord", ENGLISH + 3), eq("korWord", KOREAN + 3));

    private PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);
    private List<Vocab> vocabList = getVocabList();

    @Mock
    private MongoCollection<Vocab> mongoCollection;

    @Mock
    private FindIterable<Vocab> mongoFind;

    @InjectMocks
    private MongoDBVocabManager manager;

    @BeforeEach
    public void setup() {
        map.clear();
        when(mongoCollection.find()).thenReturn(mongoFind);
        when(mongoFind.into(any())).thenReturn(vocabList);
    }

    @Test
    public void whenLoadMongoVocabsIntoMap_givenVocabsPresentInDB_thenLoadsVocabsIntoMap() {
        manager.loadMongoVocabsIntoMap(map);

        assertEquals(1, map.size());
        assertEquals(3, map.get(LOWEST_PRIORITY).size());
        assertTrue(map.get(LOWEST_PRIORITY).contains(VOCAB1));
        assertTrue(map.get(LOWEST_PRIORITY).contains(VOCAB2));
        assertTrue(map.get(LOWEST_PRIORITY).contains(VOCAB3));
    }

    @Test
    public void whenLoadMongoVocabsIntoMap_givenNoVocabsInDB_thenMapStaysEmpty() {
        when(mongoFind.into(any())).thenReturn(Lists.emptyList());
        assertEquals(map.size(), 0);

        manager.loadMongoVocabsIntoMap(map);

        assertEquals(map.size(), 0);
    }

    @Test
    public void whenUpdatePrioritiesInDatabase_givenVocabSet_thenUpdatesInDatabase() {
        Set<Vocab> vocabSet = new HashSet<>();
        vocabSet.addAll(vocabList);

        manager.updatePrioritiesInDatabase(vocabSet);

        ArgumentCaptor<Bson> bsonCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<Document> docCaptor = ArgumentCaptor.forClass(Document.class);

        verify(mongoCollection, times(3)).updateOne(bsonCaptor.capture(), docCaptor.capture());
        assertEquals(bsonCaptor.getAllValues().size(), getUpdateFilterList().size());
        assertTrue(docCaptor.getValue().equals(LOWEST_PRIORITY_SETTER));
    }

    @Test
    public void whenUpdatePrioritiesInDatabase_givenEmptySet_thenNoUpdatesInDatabase() {
        Set<Vocab> vocabSet = new HashSet<>();

        manager.updatePrioritiesInDatabase(vocabSet);

        ArgumentCaptor<Bson> bsonCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<Document> docCaptor = ArgumentCaptor.forClass(Document.class);

        verify(mongoCollection, times(0)).updateOne(bsonCaptor.capture(), docCaptor.capture());
        assertEquals(0, bsonCaptor.getAllValues().size());
        assertEquals(0, docCaptor.getAllValues().size());
    }

    @Test
    public void whenAddNewVocabsToDatabase_givenVocabSet_thenAddsToDatabase() {
        Set<Vocab> vocabSet = new HashSet<>();
        vocabSet.addAll(vocabList);

        manager.addNewVocabsToDatabase(vocabSet);

        ArgumentCaptor<Vocab> vocabCaptor = ArgumentCaptor.forClass(Vocab.class);

        verify(mongoCollection, times(vocabSet.size())).insertOne(vocabCaptor.capture());
        assertTrue(vocabCaptor.getAllValues().containsAll(vocabSet));
        assertEquals(vocabSet.size(), vocabCaptor.getAllValues().size());
    }

    @Test
    public void whenAddNewVocabsToDatabase_givenNullSet_thenAddsToDatabase() {
        Set<Vocab> vocabSet = new HashSet<>();

        manager.addNewVocabsToDatabase(vocabSet);

        ArgumentCaptor<Vocab> vocabCaptor = ArgumentCaptor.forClass(Vocab.class);

        verify(mongoCollection, times(0)).insertOne(vocabCaptor.capture());
        assertEquals(0, vocabCaptor.getAllValues().size());
    }

    private List<Bson> getUpdateFilterList() {
        List<Bson> list = new ArrayList<>();
        list.add(VOCAB1_FILTER);
        list.add(VOCAB2_FILTER);
        list.add(VOCAB3_FILTER);
        return list;
    }

    private List<Vocab> getVocabList() {
        List<Vocab> list = new ArrayList<>();
        list.add(VOCAB1);
        list.add(VOCAB2);
        list.add(VOCAB3);
        return list;
    }
}
