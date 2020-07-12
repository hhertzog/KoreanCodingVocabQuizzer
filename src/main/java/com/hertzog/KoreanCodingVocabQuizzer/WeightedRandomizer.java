package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class WeightedRandomizer {
    Random random;
    private int[] weightedArray;

    public WeightedRandomizer(int highestWeight) {
        if (highestWeight < 1) {
            throw new IllegalArgumentException("Highest weight must be greater than or equal to 1");
        }
        this.random = new Random();
        this.weightedArray = initializeArray(highestWeight);
    }

    public int getWeightedRandomInt() {
        // random value from weighted array
        return weightedArray[random.nextInt(weightedArray.length)];
    }

    private int[] initializeArray(int highestWeight) {
        int[] weightedArray = new int[getArraySize(highestWeight)];
        int arrIndex = 0;
        int curval = 1;
        while (arrIndex < weightedArray.length) {
            for (int i = 0; i < curval; i++) {
                weightedArray[arrIndex] = curval;
                arrIndex++;
            }
            curval++;
        }
        return weightedArray;
    }

    private int getArraySize(int bound) {
        int size = 0;
        while (bound > 0) {
            size += bound;
            bound--;
        }
        return size;
    }
}
