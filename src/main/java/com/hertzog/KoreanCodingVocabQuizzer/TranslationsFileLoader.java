package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TranslationsFileLoader {

    public void loadAllVocabsFromFileIntoMap(@NonNull String filePath,
                                             @NonNull PriorityVocabMap map) {
        try {
            Scanner fileReader = new Scanner(new File(filePath));
            loadAllVocabsIntoPriorityMap(fileReader, map);
        } catch (FileNotFoundException | IllegalArgumentException e) {
            // we may want to give the user another chance to provide an existing file; no exception
            System.err.println("Could not load vocabulary into map.\n" + e.getMessage());
        }
    }

    private void loadAllVocabsIntoPriorityMap(Scanner fileReader, PriorityVocabMap map) {
        while (fileReader.hasNextLine()) {
            String vocabLine = fileReader.nextLine();
            map.put(getLinePriority(vocabLine), parseVocabList(vocabLine));
        }
    }

    private int getLinePriority(String vocabLine) {
        return Integer.parseInt(vocabLine.substring(1, 2));
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
