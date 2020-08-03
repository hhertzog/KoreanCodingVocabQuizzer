package com.hertzog.KoreanCodingVocabQuizzer;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;

// a POJO to store English and Korean translations of the vocabulary,
// with a priority level that increases the more often the user guesses this word wrong
// and decreases when the user gets the word right.
@Entity
public class Vocab {
    @BsonProperty(value = "engWord")
    private String engWord;

    @BsonProperty(value = "korWord")
    private String korWord;

    @BsonProperty(value = "priority")
    private int priority;

    public Vocab(int priority, @NonNull String engWord, @NonNull String korWord) {
        this.engWord = engWord;
        this.korWord = korWord;
        this.priority = priority;
    }

    public String getEngWord() {
        return this.engWord;
    }

    public String getKorWord() {
        return this.korWord;
    }

    public int getPriority() {
        return this.priority;
    }

    public static Vocab parseVocabFromString(@NonNull String vocabString) throws IllegalArgumentException {
        if (!vocabString.startsWith("[") || !vocabString.endsWith("]") || !vocabString.contains(":")) {
            throw new IllegalArgumentException("String \"" + vocabString + "\" is not in a parsable format");
        }
        String[] parsedVocab = vocabString.replaceAll("\\[|\\]", "").split(":");
        return new Vocab(Integer.parseInt(parsedVocab[0].trim()), parsedVocab[1].trim(), parsedVocab[2].trim());
    }

    @Override
    public String toString() {
        return  "[" + this.priority + " : " + this.engWord + " : " + this.korWord + "]";
    }
}
