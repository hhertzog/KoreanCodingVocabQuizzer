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
        return new QuizManager(priorityVocabMap(), weightedRandomizer());
    }

    @Bean
    public PriorityVocabMap priorityVocabMap() {
        return new PriorityVocabMap();
    }

    @Bean
    public WeightedRandomizer weightedRandomizer() {
        return new WeightedRandomizer(highestPriority());
    }

    public int highestPriority() {
        return Integer.parseInt(getConfigValue("highestPriority"));
    }

    private String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
