package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
    public TranslationsFileLoader translationsFileLoader() {
        return new TranslationsFileLoader();
    }

    @Bean
    public MongoDBVocabLoader mongoDBVocabLoader() {
        return new MongoDBVocabLoader(mongoClient(), mongoDatabase(), collectionName());
    }

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        String connection = String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                mongoUsername(), mongoPassword(), mongoHost(), mongoDatabase());
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connection))
                .codecRegistry(codecRegistry)
                .build();
        return MongoClients.create(clientSettings);
    }

    public String collectionName() {
        return getConfigValue("spring.data.mongodb.collection");
    }

    public int highestPriority() {
        return Integer.parseInt(getConfigValue("highestPriority"));
    }

    public int lowestPriority() {
        return Integer.parseInt(getConfigValue("lowestPriority"));
    }

    private String mongoHost() {
        return getConfigValue("spring.data.mongodb.host");
    }

    private String mongoDatabase() {
        return getConfigValue("spring.data.mongodb.database");
    }

    private String mongoUsername() {
        return getConfigValue("spring.data.mongodb.username");
    }

    private String mongoPassword() {
        return getConfigValue("spring.data.mongodb.password");
    }

    private String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
