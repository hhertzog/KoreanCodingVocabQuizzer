package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.fail;

@ActiveProfiles("test")
@SpringBootTest
public class VocabTests {
    private final String TEST_ENGLISH = "hello";
    private final String TEST_KOREAN  = "안녕하세요";
    private final int TEST_PRIORITY = 1;
    private final String EXPECTED_STRING = "[" + TEST_PRIORITY + " : " + TEST_ENGLISH + " : " + TEST_KOREAN + "]";

    private Vocab testVocab = new Vocab(TEST_PRIORITY, TEST_ENGLISH, TEST_KOREAN);
    private Vocab emptyVocab = new Vocab(TEST_PRIORITY, null, null);

    @Test
    public void whenGetEngWord_givenEnglishPresent_thenReturnEnglish() {
        assertThat(testVocab.getEngWord()).isEqualTo(TEST_ENGLISH);
    }

    @Test
    public void whenGetKorWord_givenKoreanPresent_thenReturnKorean() {
        assertThat(testVocab.getKorWord()).isEqualTo(TEST_KOREAN);
    }

    @Test
    public void whenGetEngWord_givenNullEnglish_thenReturnNull() {
        assertThat(emptyVocab.getEngWord()).isEqualTo(null);
    }

    @Test
    public void whenGetKorWord_givenNullKorean_thenReturnNull() {
        assertThat(emptyVocab.getKorWord()).isEqualTo(null);
    }

    @Test
    public void whenGetPriority_givenPriorityPresent_thenReturnPriority() {
        assertThat(testVocab.getPriority()).isEqualTo(TEST_PRIORITY);
        assertThat(emptyVocab.getPriority()).isEqualTo(TEST_PRIORITY);
    }

    @Test
    public void whenSetPriority_givenGoodPriority_thenChangePriority() {
        testVocab.setPriority(TEST_PRIORITY + 1);
        assertThat(testVocab.getPriority()).isEqualTo(TEST_PRIORITY + 1);
        testVocab.setPriority(TEST_PRIORITY);
        assertThat(testVocab.getPriority()).isEqualTo(TEST_PRIORITY);
    }

    @Test
    public void whenToString_givenPresentValues_thenReturnProperString() {
        assertThat(testVocab.toString()).isEqualTo(EXPECTED_STRING);
    }

    @Test
    public void whenParseVocabFromString_givenProperlyFormattedString_thenReturnVocab() {
        Vocab result = Vocab.parseVocabFromString(EXPECTED_STRING);
        assertThat(result.getEngWord()).isEqualTo(TEST_ENGLISH);
        assertThat(result.getKorWord()).isEqualTo(TEST_KOREAN);
    }

    @Test
    public void whenParseVocabFromString_givenBadString_thenThrowException() {
        try {
            Vocab.parseVocabFromString("]bad string[");
            fail("did not throw illegal state exception");
        } catch (IllegalArgumentException e) {
        }
    }


    @Test
    public void whenHashCode_givenPrioritiesDifferent_thenHashCodesAreEqual() {
        Vocab lowPriorityVocab = new Vocab(1, TEST_ENGLISH, TEST_KOREAN);
        Vocab highPriorityVocab = new Vocab(3, TEST_ENGLISH, TEST_KOREAN);
        HashSet<Vocab> vocabSet = new HashSet<>();
        vocabSet.add(lowPriorityVocab);
        vocabSet.add(highPriorityVocab);

        assertThat(lowPriorityVocab.hashCode() == highPriorityVocab.hashCode());
        assertThat(vocabSet.size() == 1);
    }

    @Test
    public void whenHashCode_givenDifferentTranslations_thenHashCodesAreDifferent() {
        Vocab vocabOne = new Vocab(1, TEST_ENGLISH, TEST_KOREAN);
        Vocab vocabTwo = new Vocab(1, TEST_ENGLISH + 1, TEST_KOREAN + 1);
        HashSet<Vocab> vocabSet = new HashSet<>();
        vocabSet.add(vocabOne);
        vocabSet.add(vocabTwo);

        assertThat(vocabOne.hashCode() != vocabTwo.hashCode());
        assertThat(vocabSet.size() == 2);
    }

}
