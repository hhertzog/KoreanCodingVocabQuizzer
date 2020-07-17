package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class WeightedRandomizerTests {
    private static final int TEST_WEIGHT = 4;
    private static final int ILLEGAL_WEIGHT = -1;

    private WeightedRandomizer randomizer = new WeightedRandomizer(TEST_WEIGHT);

    @Test
    public void whenCreateRandomizer_givenNegativeHighestWeight_thenThrowException() {
        try {
            WeightedRandomizer badRandomizer = new WeightedRandomizer(ILLEGAL_WEIGHT);
            fail("did not throw illegal arg exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void whenGetWeightedRandomInt_givenProperInitialization_returnInt() {
        assertThat(randomizer.getWeightedRandomInt()).isLessThanOrEqualTo(TEST_WEIGHT);
    }
}
