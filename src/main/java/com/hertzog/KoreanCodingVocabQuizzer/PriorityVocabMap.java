package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PriorityVocabMap extends HashMap<Integer, List<Vocab>> {
    private int lowestPriority;
    private int highestPriority;

    public PriorityVocabMap(int lowestPriority, int highestPriority) {
        this.lowestPriority = lowestPriority;
        this.highestPriority = highestPriority;
    }

    @Override
    public List<Vocab> put(Integer priority, @NonNull List<Vocab> vocabList) throws IllegalArgumentException {
        if (vocabList.size() == 0 || priority < lowestPriority || priority > highestPriority) {
            throw new IllegalArgumentException("List must contain at least one vocab" +
                    " & have a priority between " + lowestPriority + " and " + highestPriority);
        }
        List<Vocab> oldList = this.get(priority);
        super.put(priority, vocabList);
        return oldList;
    }

    public void addVocab(int priority, @NonNull Vocab vocab) throws IllegalArgumentException{
        if (priority < lowestPriority || priority > highestPriority) {
            throw new IllegalArgumentException("Priority must be between " + lowestPriority
                    + " and " + highestPriority);
        }

        if (this.containsKey(priority)) {
            this.get(priority).add(vocab);
        } else {
            this.put(priority, new ArrayList<>(Arrays.asList(vocab)));
        }
    }

    // if removing the vocab leaves its priority list empty, remove the list from the map
    // returns the vocab's old priority
    public int removeVocab(@NonNull Vocab vocab) throws IllegalArgumentException {
        int priority = findVocabPriority(vocab);
        this.get(priority).remove(vocab);
        if (this.get(priority).isEmpty()) {
            this.remove(priority);
        }
        return priority;
    }

    public void incrementVocabPriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        changeVocabPriority(vocab, + 1);
    }

    public void decrementVocabPriority(@NonNull Vocab vocab) throws IllegalArgumentException {
        changeVocabPriority(vocab, - 1);
    }

    private void changeVocabPriority(Vocab vocab, int priorityChange) throws IllegalArgumentException {
        int currentPriority = removeVocab(vocab);
        addVocab(currentPriority + priorityChange, vocab);
    }

    private int findVocabPriority(Vocab vocab) throws IllegalArgumentException {
        for (Entry<Integer, List<Vocab>> entry : this.entrySet()) {
            if (entry.getValue().contains(vocab)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Vocab " + vocab.getEngWord() + " not in map.");
    }

    public String toString() {
        String result = "";
        for (Entry<Integer, List<Vocab>> entry : this.entrySet()) {
            result = result + "Vocabs for priority " + entry.getKey() + ": \n" +
                    entry.getValue().toString();
        }
        return result.trim();
    }
}
