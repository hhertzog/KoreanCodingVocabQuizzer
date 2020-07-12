package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;


public class QuizManager {
    private PriorityVocabMap vocabMap;
    private WeightedRandomizer prioritySelector;
    private Random vocabSelector;

    @Autowired
    public QuizManager(PriorityVocabMap vocabMap, WeightedRandomizer weightedRandomizer) {
        this.vocabMap = vocabMap;
        this.prioritySelector = weightedRandomizer;
        this.vocabSelector = new Random();
    }
}
