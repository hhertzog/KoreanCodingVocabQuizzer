package com.hertzog.KoreanCodingVocabQuizzer;

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

    public int highestPriority() {
        return Integer.parseInt(getConfigValue("highestPriority"));
    }

    public int lowestPriority() {
        return Integer.parseInt(getConfigValue("lowestPriority"));
    }

    private String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
