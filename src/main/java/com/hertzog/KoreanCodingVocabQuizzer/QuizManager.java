package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Random;


public class QuizManager {
    private PriorityVocabMap vocabMap;
    private WeightedRandomizer prioritySelector;
    private Random vocabSelector;

    @Autowired
    public QuizManager(@NonNull PriorityVocabMap vocabMap, @NonNull WeightedRandomizer weightedRandomizer) {
        this.vocabMap = vocabMap;
        this.prioritySelector = weightedRandomizer;
        this.vocabSelector = new Random();
    }

    public Vocab getRandomVocab() throws IllegalStateException {
        if (vocabMap.isEmpty()) {
            throw new IllegalStateException("Map is empty; cannot retrieve vocabs.");
        }
        int priority = prioritySelector.getWeightedRandomInt();
        while (!vocabMap.keySet().contains(priority)) {
            priority = prioritySelector.getWeightedRandomInt();
        }

        List<Vocab> list = vocabMap.get(priority);
        System.err.println("full vocab list?" + (list == null));
        int randomVocabIndex = vocabSelector.nextInt(vocabMap.get(priority).size());
        return vocabMap.get(priority).get(randomVocabIndex);
    }

    public void raisePriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        vocabMap.incrementVocabPriority(vocab);
    }

    public void lowerPriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        vocabMap.decrementVocabPriority(vocab);
    }
}
