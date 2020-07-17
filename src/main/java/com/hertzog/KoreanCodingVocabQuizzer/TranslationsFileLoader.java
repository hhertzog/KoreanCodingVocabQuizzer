package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TranslationsFileLoader {

    public void loadAllVocabsFromFileIntoMap(int startingPriority,
                                             @NonNull String filePath,
                                             @NonNull PriorityVocabMap map) {
        try {
            Scanner fileReader = new Scanner(new File(filePath));
            loadAllVocabsIntoPriorityMap(startingPriority, fileReader, map);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            // we may want to give the user another chance to provide an existing file; no exception
            System.err.println("Could not load vocabulary into map.\n" + e.getMessage());
        }
    }

    private void loadAllVocabsIntoPriorityMap(int startingPriority, Scanner fileReader, PriorityVocabMap map) {
        int currentPriority = startingPriority;
        while (fileReader.hasNextLine()) {
            map.put(currentPriority, parseVocabList(fileReader.nextLine()));
            currentPriority++;
        }
    }

    private List<Vocab> parseVocabList(String vocabLine) {
        List<Vocab> vocabList = new ArrayList<>();
        String[] vocabArray = vocabLine.split(",");

        for (String vocabString : vocabArray) {
            vocabList.add(Vocab.parseVocabFromString(vocabString.trim()));
        }
        return vocabList;
    }
}
