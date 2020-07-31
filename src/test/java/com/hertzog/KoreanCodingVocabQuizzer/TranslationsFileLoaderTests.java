package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TranslationsFileLoaderTests {
    private String NONEXISTENT_FILE_PATH = "nonexistent file";
    private int LOWEST_PRIORITY = 1;
    private int HIGHEST_PRIORITY = 3;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";
    private static final Vocab VOCAB1 = new Vocab(ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(ENGLISH + 3, KOREAN + 3);
    private static final String GOOD_FILE = System.getProperty("user.dir") +
            "\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\translations";
    private static final String BAD_FILE = System.getProperty("user.dir") +
            "\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\badFile";

    private List<Vocab> fullVocabList = getFullVocabList();

    private TranslationsFileLoader fileLoader;

    @Test
    public void whenLoadAllVocabsFromFileIntoMap_givenProperlyFormattedFile_thenLoadsVocabsIntoMap() {
        fileLoader = new TranslationsFileLoader();
        PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);

        fileLoader.loadAllVocabsFromFileIntoMap(LOWEST_PRIORITY, GOOD_FILE, map);
        assertThat(map.entrySet().containsAll(getFilledMap().entrySet()));
    }

    @Test
    public void whenLoadAllVocabsFromFileIntoMap_givenImproperlyFormattedFile_thenNothingAddedToMap() {
        fileLoader = new TranslationsFileLoader();
        PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);
        fileLoader.loadAllVocabsFromFileIntoMap(LOWEST_PRIORITY, BAD_FILE, map);
        assertThat(map.isEmpty());
    }

    @Test
    public void whenLoadAllVocabsFromFileIntoMap_givenNonexistentFilePath_thenNothingAddedToMap() {
        fileLoader = new TranslationsFileLoader();
        PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);
        fileLoader.loadAllVocabsFromFileIntoMap(LOWEST_PRIORITY, NONEXISTENT_FILE_PATH, map);
        assertThat(map.isEmpty());
    }

    private PriorityVocabMap getFilledMap() {
        PriorityVocabMap filledMap = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);
        filledMap.put(LOWEST_PRIORITY, fullVocabList);
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
