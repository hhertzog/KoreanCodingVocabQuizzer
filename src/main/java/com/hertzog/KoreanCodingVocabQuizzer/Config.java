package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@ComponentScan("com.hertzog.KoreanCodingVocabQuizzer")
@PropertySource("classpath:application.properties")
public class Config {
    @Autowired
    private Environment env;

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.username}")
    private String mongoUsername;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;

    @Value("${spring.data.mongodb.collection}")
    private String mongoCollection;

    @Bean
    public QuizManager quizManager() {
        return new QuizManager(priorityVocabMap(), weightedRandomizer(), mongoDBVocabLoader());
    }

    @Bean
    public PriorityVocabMap priorityVocabMap() {
        return new PriorityVocabMap(lowestPriority(), highestPriority());
    }

    @Bean
    public WeightedRandomizer weightedRandomizer() {
        return new WeightedRandomizer(highestPriority());
    }

    @Bean
    public MongoDBVocabManager mongoDBVocabLoader() {
        return new MongoDBVocabManager(mongoCollection());
    }

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        String connection = String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                mongoUsername, mongoPassword, mongoHost, mongoDatabase);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connection))
                .codecRegistry(codecRegistry)
                .build();
        return MongoClients.create(clientSettings);
    }

    @Bean
    public MongoCollection<Vocab> mongoCollection() {
        return mongoClient().getDatabase(mongoDatabase).getCollection(mongoCollection, Vocab.class);
    }

    private int highestPriority() {
        return Integer.parseInt(getConfigValue("highestPriority"));
    }

    private int lowestPriority() {
        return Integer.parseInt(getConfigValue("lowestPriority"));
    }

    private String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
