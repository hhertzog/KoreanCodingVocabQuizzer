package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class VocabTests {
    private final String TEST_ENGLISH = "hello";
    private final String TEST_KOREAN  = "안녕하세요";
    private final String EXPECTED_STRING = "[" + TEST_ENGLISH + " : " + TEST_KOREAN + "]";

    private Vocab testVocab = new Vocab(TEST_ENGLISH, TEST_KOREAN);
    private Vocab emptyVocab = new Vocab(null, null);

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
    public void whenToString_givenPresentValues_thenReturnProperString() {
        assertThat(testVocab.toString()).isEqualTo(EXPECTED_STRING);
    }

}
