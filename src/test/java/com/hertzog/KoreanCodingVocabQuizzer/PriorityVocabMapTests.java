package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class PriorityVocabMapTests {
    private static final int LOWEST_PRIORITY = 1;
    private static final int HIGHEST_PRIORITY = 3;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";

    private static final Vocab VOCAB1 = new Vocab(ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(ENGLISH + 3, KOREAN + 3);

    private PriorityVocabMap map = new PriorityVocabMap(LOWEST_PRIORITY, HIGHEST_PRIORITY);

    @BeforeEach
    public void runBefore() {
        map.clear();
    }

    @Test
    public void whenPutVocabList_givenFilledVocabListAndNoPreviousList_thenReturnNull() {
        assertThat(map.put(LOWEST_PRIORITY, getFullVocabList())).isNull();
    }

    @Test
    public void whenPutVocabList_givenListAlreadyPresent_thenReturnOldList() {
        assertThat(map.put(LOWEST_PRIORITY, getSingleVocabList())).isNull();
        assertThat(map.put(LOWEST_PRIORITY, getFullVocabList())).isEqualTo(getSingleVocabList());
    }

    @Test
    public void whenPutVocabList_givenEmptyList_thenThrowIllegalArgException() {
        try {
            map.put(LOWEST_PRIORITY, Collections.EMPTY_LIST);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenPutVocabList_givenIllegalPriority_thenThrowIllegalArgException() {
        try {
            map.put(LOWEST_PRIORITY - 1, getFullVocabList());
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenAddVocab_givenFirstVocabForPriority_thenNewListCreated() {
        assertThat(map.get(LOWEST_PRIORITY)).isNull();
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        assertThat(map.get(LOWEST_PRIORITY)).isEqualTo(Collections.singletonList(VOCAB1));
    }

    @Test
    public void whenAddVocab_givenIllegalPriority_thenThrowIllegalArgException() {
        try {
            map.addVocab(HIGHEST_PRIORITY + 1, VOCAB1);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenAddVocab_givenPriorityListExists_thenVocabAddedToList() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        map.addVocab(LOWEST_PRIORITY, VOCAB2);
        map.addVocab(LOWEST_PRIORITY, VOCAB3);
        assertThat(map.get(LOWEST_PRIORITY)).isEqualTo(getFullVocabList());
    }

    @Test
    public void whenRemoveVocab_givenVocabInMapWithOthersInPriorityList_thenReturnOldPriority() {
        map.put(LOWEST_PRIORITY, getFullVocabList());
        assertThat(map.get(LOWEST_PRIORITY).contains(VOCAB1));
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(LOWEST_PRIORITY);
        assertThat(map.get(LOWEST_PRIORITY)).doesNotContain(VOCAB1);
        assertThat(map.get(LOWEST_PRIORITY)).contains(VOCAB2, VOCAB3);
    }

    @Test
    public void whenRemoveVocab_givenSingleVocabInPriorityList_thenReturnOldPriority() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(LOWEST_PRIORITY);
        assertThat(!map.containsKey(LOWEST_PRIORITY));
    }

    @Test
    public void whenRemoveVocab_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        try {
            map.removeVocab(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenIncrementVocabPriority_givenNewPriorityListNotInMap_thenCreateNewPriorityList() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        assertThat(!map.containsKey(LOWEST_PRIORITY + 1));
        map.incrementVocabPriority(VOCAB1);
        assertThat(map.get(LOWEST_PRIORITY + 1)).contains(VOCAB1);
    }

    @Test
    public void whenIncrementVocabPriority_givenNewPriorityListInMap_thenAddToNewPriorityList() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        map.addVocab(LOWEST_PRIORITY + 1, VOCAB2);
        assertThat(map.containsKey(LOWEST_PRIORITY + 1));
        map.incrementVocabPriority(VOCAB1);
        assertThat(map.get(LOWEST_PRIORITY + 1)).contains(VOCAB1, VOCAB2);
    }

    @Test
    public void whenIncrementVocabPriority_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        try {
            map.incrementVocabPriority(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenIncrementVocabPriority_givenVocabHasHighestPriority_thenDoesntChangePriority() {
        map.addVocab(HIGHEST_PRIORITY, VOCAB1);
        map.incrementVocabPriority(VOCAB1);
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(HIGHEST_PRIORITY);
    }

    @Test
    public void whenDecrementVocabPriority_givenNewPriorityListNotInMap_thenCreateNewPriorityList() {
        map.addVocab(HIGHEST_PRIORITY, VOCAB1);
        assertThat(!map.containsKey(HIGHEST_PRIORITY - 1));
        map.decrementVocabPriority(VOCAB1);
        assertThat(map.get(HIGHEST_PRIORITY - 1)).contains(VOCAB1);
    }

    @Test
    public void whenDecrementVocabPriority_givenNewPriorityListInMap_thenAddToNewPriorityList() {
        map.addVocab(HIGHEST_PRIORITY, VOCAB1);
        map.addVocab(HIGHEST_PRIORITY - 1, VOCAB2);
        assertThat(map.containsKey(HIGHEST_PRIORITY - 1));
        map.decrementVocabPriority(VOCAB1);
        assertThat(map.get(HIGHEST_PRIORITY - 1)).contains(VOCAB1, VOCAB2);
    }

    @Test
    public void whenDecrementVocabPriority_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        try {
            map.decrementVocabPriority(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenDecrementVocabPriority_givenVocabHasLowestPriority_thenDoesntChangePriority() {
        map.addVocab(LOWEST_PRIORITY, VOCAB1);
        map.decrementVocabPriority(VOCAB1);
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(LOWEST_PRIORITY);
    }

    @Test
    public void whenToString_givenFilledMap_thenReturnProperString() {
        map.put(LOWEST_PRIORITY, getFullVocabList());
        assertThat(map.toString()).isEqualTo(getExpectedFullMapString());
    }

    @Test
    public void whenGetLowestPriority_givenInitializedMap_thenReturnLowestPriority() {
        assertThat(map.getLowestPriority()).isEqualTo(LOWEST_PRIORITY);
    }

    @Test
    public void whenGetHighestPriority_givenInitializedMap_thenReturnHighestPriority() {
        assertThat(map.getHighestPriority()).isEqualTo(HIGHEST_PRIORITY);
    }

    @Test
    public void whenToString_givenEmptyMap_thenReturnEmptyString() {
        assertThat(map.toString()).isEqualTo("");
    }

    private String getExpectedFullMapString() {
        return "Vocabs for priority " + LOWEST_PRIORITY + ": \n" + getFullVocabList().toString();
    }

    private List<Vocab> getSingleVocabList() {
        return Collections.singletonList(VOCAB1);
    }

    private List<Vocab> getFullVocabList() {
        List<Vocab> list = new ArrayList<>();
        list.add(VOCAB1);
        list.add(VOCAB2);
        list.add(VOCAB3);
        return list;
    }
}
