package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;

// a POJO to store English and Korean translations of the vocabulary,
// with a priority level that increases the more often the user guesses this word wrong
// and decreases when the user gets the word right.
@Entity
public class Vocab {
    private String engWord;
    private String korWord;

    public Vocab(@NonNull String engWord, @NonNull String korWord) {
        this.engWord = engWord;
        this.korWord = korWord;
    }

    public String getEngWord() {
        return this.engWord;
    }

    public String getKorWord() {
        return this.korWord;
    }

    public static Vocab parseVocabFromString(@NonNull String vocabString) throws IllegalArgumentException {
        if (!vocabString.startsWith("[") || !vocabString.endsWith("]") || !vocabString.contains(":")) {
            throw new IllegalArgumentException("String \"" + vocabString + "\" is not in a parsable format");
        }
        String[] englishAndKoreanVocabs = vocabString.replaceAll("\\[|\\]", "").split(":");
        return new Vocab(englishAndKoreanVocabs[0].trim(), englishAndKoreanVocabs[1].trim());
    }

    @Override
    public String toString() {
        return  "[" + this.engWord + " : " + this.korWord + "]";
    }
}
