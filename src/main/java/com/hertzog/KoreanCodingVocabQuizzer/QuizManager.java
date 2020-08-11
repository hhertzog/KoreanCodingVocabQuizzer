package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizManager {
    private PriorityVocabMap vocabMap;
    private WeightedRandomizer prioritySelector;
    private MongoDBVocabManager dbManager;
    private Random vocabSelector;

    @Autowired
    public QuizManager(@NonNull PriorityVocabMap vocabMap,
                       @NonNull WeightedRandomizer weightedRandomizer,
                       @NonNull MongoDBVocabManager dbManager) {
        this.vocabMap = vocabMap;
        this.prioritySelector = weightedRandomizer;
        this.dbManager = dbManager;
        this.vocabSelector = new Random();
    }

    public void loadVocabs() {
        dbManager.loadMongoVocabsIntoMap(vocabMap);
    }

    public Vocab getRandomVocab() throws IllegalStateException {
        if (vocabMap.isEmpty()) {
            throw new IllegalStateException("Map is empty; cannot retrieve vocabs.");
        }

        int priorityToQuiz = getRandomPriorityPresentInMap();
        return getRandomVocabForPriority(priorityToQuiz);
    }

    public void updateDatabase(Set<Vocab> vocabSet) {
        dbManager.updatePrioritiesInDatabase(vocabSet);
    }

    public void addVocabsToDatabase(Set<Vocab> vocabSet) {
        dbManager.addNewVocabsToDatabase(vocabSet);
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