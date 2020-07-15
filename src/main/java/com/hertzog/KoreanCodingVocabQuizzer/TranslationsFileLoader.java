package com.hertzog.KoreanCodingVocabQuizzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TranslationsFileLoader {
    private Integer startingPriority;
    private Scanner fileReader;

    public TranslationsFileLoader(int startingPriority, String filePath) throws FileNotFoundException {
        this.startingPriority = startingPriority;
        fileReader = new Scanner(new File(filePath));
    }

    public void loadAllVocabsIntoPriorityMap(PriorityVocabMap map) {
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
