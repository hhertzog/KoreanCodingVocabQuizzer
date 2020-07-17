package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Random;

public class QuizManager {
    private PriorityVocabMap vocabMap;
    private WeightedRandomizer prioritySelector;
    private TranslationsFileLoader fileLoader;
    private Random vocabSelector;

    @Autowired
    public QuizManager(@NonNull PriorityVocabMap vocabMap,
                       @NonNull WeightedRandomizer weightedRandomizer,
                       @NonNull TranslationsFileLoader fileLoader) {
        this.vocabMap = vocabMap;
        this.prioritySelector = weightedRandomizer;
        this.fileLoader = fileLoader;
        this.vocabSelector = new Random();
    }

    //TODO: implement loading vocabs + add junit tests
    public void loadVocabs(String filePath) {
        //TranslationsFileLoader loader = new TranslationsFileLoader(filePath);
    }

    public Vocab getRandomVocab() throws IllegalStateException {
        if (vocabMap.isEmpty()) {
            throw new IllegalStateException("Map is empty; cannot retrieve vocabs.");
        }
        int priority = prioritySelector.getWeightedRandomInt();
        while (!vocabMap.keySet().contains(priority)) {
            priority = prioritySelector.getWeightedRandomInt();
        }

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
