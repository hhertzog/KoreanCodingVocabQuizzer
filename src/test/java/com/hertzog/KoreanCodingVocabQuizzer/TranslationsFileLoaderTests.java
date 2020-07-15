package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TranslationsFileLoaderTests {
    private String testPath = "C:\\Users\\hhert\\IdeaProjects\\KoreanCodingVocabQuizzer\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\translations";
    private int startingPriority = 2;
    private TranslationsFileLoader fileLoader;

    @Test
    public void initialize() throws Exception {
        fileLoader = new TranslationsFileLoader(startingPriority, testPath);
    }

}
