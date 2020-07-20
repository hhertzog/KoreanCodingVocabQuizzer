package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class KoreanCodingVocabQuizzerApplication {
	private static Scanner input = new Scanner(System.in);
	private static QuizManager quizManager;

	public static void main(String[] args) {
		ConfigurableApplicationContext appContext =
				SpringApplication.run(KoreanCodingVocabQuizzerApplication.class, args);
		quizManager = appContext.getBean(QuizManager.class);

		loadVocabs();
		quizOnEnglishTranslations();
		printGoodbyeMessage();
	}

	private static void loadVocabs() {
		quizManager.loadVocabs("C:\\Users\\hhert\\IdeaProjects\\KoreanCodingVocabQuizzer\\src\\main\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\translations");
	}

	private static void quizOnEnglishTranslations() {
		boolean shouldKeepPlaying = true;
		System.out.println("Please type \"x\" when you want to quit.");

		while (shouldKeepPlaying) {
			Vocab vocabToQuiz = quizManager.getRandomVocab();

			System.out.println("\nWhat is the English translation of " + vocabToQuiz.getKorWord() + "?");
			String response = input.nextLine();

			if (response.equals("x")) {
				shouldKeepPlaying = false;
			} else {
				handleResponse(response, vocabToQuiz);
			}
		}
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
