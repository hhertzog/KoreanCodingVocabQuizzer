package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
public class QuizManagerTests {
    private QuizManager quizManager;

    @Test
    public void instantiates() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        quizManager = context.getBean(QuizManager.class);
    }
}
