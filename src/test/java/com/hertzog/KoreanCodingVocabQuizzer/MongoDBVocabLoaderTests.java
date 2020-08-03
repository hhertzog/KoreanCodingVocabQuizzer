package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MongoDBVocabLoaderTests {
    @Mock
    private MongoClient mongoClient;

    private final String databaseName = "database_name";

    private final String collectionName = "collection_name";

    @InjectMocks
    private MongoDBVocabLoader loader;

    @Test
    public void instantiates() {
    }
}
