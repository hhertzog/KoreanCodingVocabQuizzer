package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


@SpringBootTest
public class QuizManagerTests {
    private static final int LOWEST_PRIORITY = 1;
    private static final int HIGHEST_PRIORITY = 3;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";
    private static final Vocab VOCAB1 = new Vocab(LOWEST_PRIORITY, ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(LOWEST_PRIORITY, ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(LOWEST_PRIORITY, ENGLISH + 3, KOREAN + 3);
    private List<Vocab> fullVocabList = getFullVocabList();

    @Mock
    private PriorityVocabMap priorityVocabMap;

    @Mock
    private WeightedRandomizer weightedRandomizer;

    @Mock
    private MongoDBVocabLoader vocabLoader;

    @InjectMocks
    private QuizManager quizManager;

    @Test
    public void whenLoadVocabs_givenValidFilePath_thenMakesCallToFileLoader() {
        when(priorityVocabMap.getLowestPriority()).thenReturn(LOWEST_PRIORITY);
        quizManager.loadVocabs();

        verify(vocabLoader, times(1)).loadMongoVocabsIntoMap(priorityVocabMap);
    }

    @Test
    public void whenGetRandomVocab_givenFilledMap_thenReturnVocab() {
        when(priorityVocabMap.isEmpty()).thenReturn(false);
        when(priorityVocabMap.get(HIGHEST_PRIORITY)).thenReturn(fullVocabList);
        when(priorityVocabMap.keySet()).thenReturn(new HashSet<>(Arrays.asList(HIGHEST_PRIORITY)));
        when(weightedRandomizer.getWeightedRandomInt()).thenReturn(HIGHEST_PRIORITY);

        Vocab vocab = quizManager.getRandomVocab();
        assertThat(fullVocabList.contains(vocab));
    }

    @Test
    public void whenGetRandomVocab_givenEmptyMap_thenThrowException() {
        try {
            when(priorityVocabMap.isEmpty()).thenReturn(true);
            quizManager.getRandomVocab();
            fail("did not throw illegal state exception");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void whenRaisePriority_givenMapDoesntThrowException_thenNoException() {
        quizManager.raisePriority(VOCAB1);
    }

    @Test
    public void whenRaisePriority_givenMapThrowsException_thenThrowException() {
        try {
            doThrow(IllegalArgumentException.class).when(priorityVocabMap).incrementVocabPriority(VOCAB1);
            quizManager.raisePriority(VOCAB1);
            fail("did not throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenLowerPriority_givenMapDoesntThrowException_thenNoException() {
        quizManager.lowerPriority(VOCAB1);
    }

    @Test
    public void whenLowerPriority_givenMapThrowsException_thenThrowException() {
        try {
            doThrow(IllegalArgumentException.class).when(priorityVocabMap).decrementVocabPriority(VOCAB1);
            quizManager.lowerPriority(VOCAB1);
            fail("did not throw illegal argument exception");
        } catch (IllegalArgumentException e) {
        }
    }

    private List<Vocab> getFullVocabList() {
        List<Vocab> list = new ArrayList<>();
        list.add(VOCAB1);
        list.add(VOCAB2);
        list.add(VOCAB3);
        return list;
    }
}
