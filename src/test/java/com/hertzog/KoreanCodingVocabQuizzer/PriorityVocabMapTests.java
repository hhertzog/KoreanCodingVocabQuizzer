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
    private static final int PRIORITY = 2;
    private static final String ENGLISH = "english";
    private static final String KOREAN = "한국어";

    private static final Vocab VOCAB1 = new Vocab(ENGLISH + 1, KOREAN + 1);
    private static final Vocab VOCAB2 = new Vocab(ENGLISH + 2, KOREAN + 2);
    private static final Vocab VOCAB3 = new Vocab(ENGLISH + 3, KOREAN + 3);

    private PriorityVocabMap map = new PriorityVocabMap();

    @BeforeEach
    public void runBefore() {
        map.clear();
    }

    @Test
    public void whenPutVocabList_givenFilledVocabListAndNoPreviousList_thenReturnNull() {
        assertThat(map.put(PRIORITY, getFullVocabList())).isNull();
    }

    @Test
    public void whenPutVocabList_givenListAlreadyPresent_thenReturnOldList() {
        assertThat(map.put(PRIORITY, getSingleVocabList())).isNull();
        assertThat(map.put(PRIORITY, getFullVocabList())).isEqualTo(getSingleVocabList());
    }

    @Test
    public void whenPutVocabList_givenEmptyList_thenThrowIllegalArgException() {
        try {
            map.put(PRIORITY, Collections.EMPTY_LIST);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenPutVocabList_givenPriorityLessThanOne_thenThrowIllegalArgException() {
        try {
            map.put(0, getFullVocabList());
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenAddVocab_givenFirstVocabForPriority_thenNewListCreated() {
        assertThat(map.get(PRIORITY)).isNull();
        map.addVocab(PRIORITY, VOCAB1);
        assertThat(map.get(PRIORITY)).isEqualTo(Collections.singletonList(VOCAB1));
    }

    @Test
    public void whenAddVocab_givenPriorityListExists_thenVocabAddedToList() {
        map.addVocab(PRIORITY, VOCAB1);
        map.addVocab(PRIORITY, VOCAB2);
        map.addVocab(PRIORITY, VOCAB3);
        assertThat(map.get(PRIORITY)).isEqualTo(getFullVocabList());
    }

    @Test
    public void whenRemoveVocab_givenVocabInMapWithOthersInPriorityList_thenReturnOldPriority() {
        map.put(PRIORITY, getFullVocabList());
        assertThat(map.get(PRIORITY).contains(VOCAB1));
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(PRIORITY);
        assertThat(map.get(PRIORITY)).doesNotContain(VOCAB1);
        assertThat(map.get(PRIORITY)).contains(VOCAB2, VOCAB3);
    }

    @Test
    public void whenRemoveVocab_givenSingleVocabInPriorityList_thenReturnOldPriority() {
        map.addVocab(PRIORITY, VOCAB1);
        assertThat(map.removeVocab(VOCAB1)).isEqualTo(PRIORITY);
        assertThat(!map.containsKey(PRIORITY));
    }

    @Test
    public void whenRemoveVocab_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(PRIORITY, VOCAB1);
        try {
            map.removeVocab(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenIncrementVocabPriority_givenNewPriorityListNotInMap_thenCreateNewPriorityList() {
        map.addVocab(PRIORITY, VOCAB1);
        assertThat(!map.containsKey(PRIORITY + 1));
        map.incrementVocabPriority(VOCAB1);
        assertThat(map.get(PRIORITY + 1)).contains(VOCAB1);
    }

    @Test
    public void whenIncrementVocabPriority_givenNewPriorityListInMap_thenAddToNewPriorityList() {
        map.addVocab(PRIORITY, VOCAB1);
        map.addVocab(PRIORITY + 1, VOCAB2);
        assertThat(map.containsKey(PRIORITY + 1));
        map.incrementVocabPriority(VOCAB1);
        assertThat(map.get(PRIORITY + 1)).contains(VOCAB1, VOCAB2);
    }

    @Test
    public void whenIncrementVocabPriority_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(PRIORITY, VOCAB1);
        try {
            map.incrementVocabPriority(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenDecrementVocabPriority_givenNewPriorityListNotInMap_thenCreateNewPriorityList() {
        map.addVocab(PRIORITY, VOCAB1);
        assertThat(!map.containsKey(PRIORITY - 1));
        map.decrementVocabPriority(VOCAB1);
        assertThat(map.get(PRIORITY - 1)).contains(VOCAB1);
    }

    @Test
    public void whenDecrementVocabPriority_givenNewPriorityListInMap_thenAddToNewPriorityList() {
        map.addVocab(PRIORITY, VOCAB1);
        map.addVocab(PRIORITY - 1, VOCAB2);
        assertThat(map.containsKey(PRIORITY - 1));
        map.decrementVocabPriority(VOCAB1);
        assertThat(map.get(PRIORITY - 1)).contains(VOCAB1, VOCAB2);
    }

    @Test
    public void whenDecrementVocabPriority_givenVocabNotInMap_thenThrowIllegalArgException() {
        map.addVocab(PRIORITY, VOCAB1);
        try {
            map.decrementVocabPriority(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenDecrementVocabPriority_givenVocabPriorityIsOne_thenThrowIllegalArgException() {
        map.addVocab(1, VOCAB1);
        try {
            map.decrementVocabPriority(VOCAB2);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenToString_givenFilledMap_thenReturnProperString() {
        map.put(PRIORITY, getFullVocabList());
        assertThat(map.toString()).isEqualTo(getExpectedFullMapString());
    }

    @Test
    public void whenToString_givenEmptyMap_thenReturnEmptyString() {
        assertThat(map.toString()).isEqualTo("");
    }

    private String getExpectedFullMapString() {
        return "Vocabs for priority " + PRIORITY + ": \n" + getFullVocabList().toString();
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
