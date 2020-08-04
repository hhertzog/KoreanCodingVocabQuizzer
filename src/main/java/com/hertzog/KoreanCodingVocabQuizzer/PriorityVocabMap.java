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

    // puts all vocabs in vocabList in the map and changes their priorities to be the given priority.
    @Override
    public List<Vocab> put(Integer priority, @NonNull List<Vocab> vocabList) throws IllegalArgumentException {
        if (vocabList.size() == 0 || priority < lowestPriority || priority > highestPriority) {
            throw new IllegalArgumentException("List must contain at least one vocab" +
                    " & have a priority between " + lowestPriority + " and " + highestPriority);
        }

        for (Vocab vocab : vocabList) {
            vocab.setPriority(priority);
        }
        List<Vocab> oldList = this.get(priority);
        super.put(priority, vocabList);
        return oldList;
    }

    public void addVocab(@NonNull Vocab vocab) throws IllegalArgumentException{
        int priority = vocab.getPriority();
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
        if (this.get(vocab.getPriority()) == null || !this.get(vocab.getPriority()).contains(vocab)) {
            throw new IllegalArgumentException("Cannot remove vocab; vocab not present in map.");
        }

        int priority = vocab.getPriority();
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

    // if given vocab has a priority less than the highest possible priority or more than the lowest possible priority,
    // changes the vocab's priority by priorityChange amount; otherwise, leaves vocab at its current priority
    private void changeVocabPriority(Vocab vocab, int priorityChange) throws IllegalArgumentException {
        if (this.get(vocab.getPriority()) == null || !this.get(vocab.getPriority()).contains(vocab)) {
            throw new IllegalArgumentException("Cannot change priority; vocab not present in map.");
        }

        int newPriority = vocab.getPriority() + priorityChange;
        if ((highestPriority >= newPriority) && (newPriority >= lowestPriority)) {
            removeVocab(vocab);
            vocab.setPriority(newPriority);
            addVocab(vocab);
        }
    }

    public int getLowestPriority() {
        return lowestPriority;
    }

    public int getHighestPriority() {
        return highestPriority;
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
