package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest
public class TranslationsFileLoaderTests {
    private String goodPath = "C:\\Users\\hhert\\IdeaProjects\\KoreanCodingVocabQuizzer\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\translations";
    private String badPath = "C:\\Users\\hhert\\IdeaProjects\\KoreanCodingVocabQuizzer\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\badFile";
    private int startingPriority = 2;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";
    private static final Vocab VOCAB1 = new Vocab(ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(ENGLISH + 3, KOREAN + 3);
    private List<Vocab> fullVocabList = getFullVocabList();

    private TranslationsFileLoader fileLoader;

    @Test
    public void whenLoadAllVocabsIntoPriorityMap_givenProperlyFormattedFile_thenLoadsVocabsIntoMap() throws Exception {
        fileLoader = new TranslationsFileLoader(startingPriority, goodPath);
        PriorityVocabMap map = new PriorityVocabMap();

        fileLoader.loadAllVocabsIntoPriorityMap(map);
        assertThat(map.entrySet().containsAll(getFilledMap().entrySet()));
    }

    @Test
    public void whenLoadAllVocabsIntoPriorityMap_givenImproperlyFormattedFile_thenThrowsException() throws Exception {
        fileLoader = new TranslationsFileLoader(startingPriority, badPath);
        PriorityVocabMap map = new PriorityVocabMap();

        try {
            fileLoader.loadAllVocabsIntoPriorityMap(map);
            fail("did not throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
    }

    private PriorityVocabMap getFilledMap() {
        PriorityVocabMap filledMap = new PriorityVocabMap();
        filledMap.put(startingPriority, fullVocabList);
        return filledMap;
    }

    private List<Vocab> getFullVocabList() {
        List<Vocab> list = new ArrayList<>();
        list.add(VOCAB1);
        list.add(VOCAB2);
        list.add(VOCAB3);
        return list;
    }
}
