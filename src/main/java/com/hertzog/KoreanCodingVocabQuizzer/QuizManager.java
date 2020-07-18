package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.List;
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

    public void loadVocabs(String filePath) {
        fileLoader.loadAllVocabsFromFileIntoMap(vocabMap.getLowestPriority(), filePath, vocabMap);
    }

    public Vocab getRandomVocab() throws IllegalStateException {
        if (vocabMap.isEmpty()) {
            throw new IllegalStateException("Map is empty; cannot retrieve vocabs.");
        }

        int priorityToQuiz = getRandomPriorityPresentInMap();
        return getRandomVocabForPriority(priorityToQuiz);
    }

    public void raisePriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        vocabMap.incrementVocabPriority(vocab);
    }

    public void lowerPriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        vocabMap.decrementVocabPriority(vocab);
    }

    private Vocab getRandomVocabForPriority(int priority) {
        List<Vocab> vocabList = vocabMap.get(priority);
        return vocabList.get(vocabSelector.nextInt(vocabList.size()));
    }

    private int getRandomPriorityPresentInMap() {
        int priority = prioritySelector.getWeightedRandomInt();
        while (!vocabMap.keySet().contains(priority)) {
            priority = prioritySelector.getWeightedRandomInt();
        }
        return priority;
    }
}
