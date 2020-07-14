package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@SpringBootTest
public class QuizManagerTests {
    private static boolean initIsDone = false;
    private static final int PRIORITY = 2;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";

    private static final Vocab VOCAB1 = new Vocab(ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(ENGLISH + 3, KOREAN + 3);
    private List<Vocab> fullVocabList = getFullVocabList();

    @Mock
    private PriorityVocabMap priorityVocabMap;

    @Mock
    private WeightedRandomizer weightedRandomizer;

    @InjectMocks
    private QuizManager quizManager;

    @Test
    public void whenGetRandomVocab_givenFilledMap_thenReturnVocab() {
        when(priorityVocabMap.isEmpty()).thenReturn(false);
        when(priorityVocabMap.get(PRIORITY)).thenReturn(fullVocabList);
        when(priorityVocabMap.keySet()).thenReturn(new HashSet<>(Arrays.asList(PRIORITY)));
        when(weightedRandomizer.getWeightedRandomInt()).thenReturn(PRIORITY);

        Vocab vocab = quizManager.getRandomVocab();
        assertThat(fullVocabList.contains(vocab));
    }

    @Test
    public void whenGetRandomVocab_givenEmptyMap_thenThrowException() {
        try {
            when(priorityVocabMap.isEmpty()).thenReturn(true);
            quizManager.getRandomVocab();
            Assert.fail("did not throw illegal state exception");
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
