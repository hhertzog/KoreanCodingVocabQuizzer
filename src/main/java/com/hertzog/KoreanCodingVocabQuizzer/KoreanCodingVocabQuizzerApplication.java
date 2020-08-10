package com.hertzog.KoreanCodingVocabQuizzer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class KoreanCodingVocabQuizzerApplication {
	private static Scanner input;
	private static QuizManager quizManager;

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext =
				SpringApplication.run(KoreanCodingVocabQuizzerApplication.class, args);
		// disable printing logs in the console
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);
		quizManager = appContext.getBean(QuizManager.class);
		input = new Scanner(System.in);

		runQuiz();
	}

	private static void runQuiz() {
        loadVocabs();
        quizOnEnglishTranslations();
        printGoodbyeMessage();
    }

	private static void loadVocabs() {
		quizManager.loadVocabs();
	}

	private static void quizOnEnglishTranslations() {
		boolean shouldKeepPlaying = true;
		Set<Vocab> vocabsSeen = new HashSet<>();
		System.out.println("Please type \"x\" when you want to quit.");

		while (shouldKeepPlaying) {
			Vocab vocabToQuiz = quizManager.getRandomVocab();

			System.out.println("\nWhat is the English translation of " + vocabToQuiz.getKorWord() + "?");
			String response = input.nextLine();

			if (response.equals("x")) {
				shouldKeepPlaying = false;
			} else {
				handleResponse(response, vocabToQuiz);
				vocabsSeen.add(vocabToQuiz);
			}
		}
		quizManager.updateDatabase(vocabsSeen);
	}

	private static void handleResponse(String response, Vocab vocabToQuiz) {
		if (answeredCorrectly(response, vocabToQuiz.getEngWord())) {
			System.out.println("Correct!");
			quizManager.lowerPriority(vocabToQuiz);
		} else {
			System.out.println("Sorry, the correct answer was " + vocabToQuiz.getEngWord());
			quizManager.raisePriority(vocabToQuiz);
		}
	}

	private static boolean answeredCorrectly(String response, String correctAnswer) {
		return response.trim().equalsIgnoreCase(correctAnswer);
	}

	private static void printGoodbyeMessage() {
		System.out.println("Thanks for playing!");
	}
}
