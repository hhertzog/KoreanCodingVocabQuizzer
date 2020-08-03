package com.hertzog.KoreanCodingVocabQuizzer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.hertzog.KoreanCodingVocabQuizzer")
@PropertySource("classpath:application.properties")
public class Config {
    @Autowired
    private Environment env;

    @Bean
    public QuizManager quizManager() {
        return new QuizManager(priorityVocabMap(), weightedRandomizer(), translationsFileLoader());
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
    public MongoClient mongoClient() {
        String connection = String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                mongoUsername(), mongoPassword(), mongoHost(), mongoDatabase());
        return new MongoClient(new MongoClientURI(connection));
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
