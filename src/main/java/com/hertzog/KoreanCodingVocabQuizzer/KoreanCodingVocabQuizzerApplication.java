package com.hertzog.KoreanCodingVocabQuizzer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static java.lang.System.exit;

@Profile("!test")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class KoreanCodingVocabQuizzerApplication implements CommandLineRunner {
	@Value("${highestPriority}")
	private Integer highestPriority;

	private Random languageChooser;
	private Scanner input;
	private QuizManager quizManager;

	@Autowired
	public KoreanCodingVocabQuizzerApplication(@NonNull QuizManager quizManager) {
		this.languageChooser = new Random();
		this.input = new Scanner(System.in);
		this.quizManager = quizManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(KoreanCodingVocabQuizzerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// disable printing mongo logs in the console
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);
		runQuiz();
		exit(0);
	}

	private void runQuiz() {
		promptForAddingNewVocab();
		loadVocabs();
		startQuiz();
        printGoodbyeMessage();
    }

	private void loadVocabs() {
		quizManager.loadVocabs();
	}

	private void promptForAddingNewVocab() {
		System.out.println("Would you like to 1) BEGIN THE QUIZ, or 2) ADD MORE VOCAB to test yourself on?");
		System.out.println("Type 1 to start quizzing and 2 to add new vocabulary.");
		String response = input.nextLine().trim();

		if (response.equals("2")) {
			addNewVocabulary();
		} else if (!response.equals("1")) {
			System.out.println("Sorry, I couldn't understand that. Please enter only a number.");
			promptForAddingNewVocab();
		}
	}

	private void startQuiz() {
		boolean shouldKeepPlaying = true;
		Set<Vocab> vocabsSeen = new HashSet<>();
		System.out.println("Please type \"x\" when you want to quit.");

		while (shouldKeepPlaying) {
			Vocab vocabToQuiz = quizManager.getRandomVocab();
			shouldKeepPlaying = playSingleQuizRound(vocabToQuiz);

			if (shouldKeepPlaying) {
				vocabsSeen.add(vocabToQuiz);
			}
		}
		quizManager.updateDatabase(vocabsSeen);
	}

	// returns true if user wants to play another round; false if not
	private boolean playSingleQuizRound(Vocab vocabToQuiz) {
		return shouldQuizOnEnglishAnswer() ?
				quizOnSingleVocab("English", vocabToQuiz.getKorWord(), vocabToQuiz.getEngWord(), vocabToQuiz) :
				quizOnSingleVocab("Korean", vocabToQuiz.getEngWord(), vocabToQuiz.getKorWord(), vocabToQuiz);
	}

	private boolean quizOnSingleVocab(String language, String quizWord, String translation, Vocab vocabToQuiz) {
		System.out.println("\nWhat is the " + language + " translation of \"" + quizWord + "\"?");
		String response = input.nextLine();
		if (!response.equalsIgnoreCase("x")) {
			handleResponse(response, translation, vocabToQuiz);
			return true;
		}
		return false;
	}

	private void addNewVocabulary() {
		Set<Vocab> wordsToAdd = new HashSet<>();
		boolean moreVocabToAdd = true;

		while (moreVocabToAdd) {
			System.out.println("Please enter the Korean and English of the word to add.");
			System.out.print("Korean: ");
			String korWord = input.nextLine().trim();
			System.out.print("\nEnglish: ");
			String engWord = input.nextLine().trim();

			wordsToAdd.add(new Vocab(highestPriority, engWord, korWord));
			System.out.println("Any more vocab to add? y/n");
			moreVocabToAdd = input.nextLine().trim().charAt(0) == 'y';
		}

		quizManager.addVocabsToDatabase(wordsToAdd);
	}

	private void handleResponse(String response, String correctAnswer, Vocab vocabToQuiz) {
		if (answeredCorrectly(response, correctAnswer)) {
			System.out.println("Correct!");
			quizManager.lowerPriority(vocabToQuiz);
		} else {
			System.out.println("Sorry, the correct answer was " + correctAnswer);
			quizManager.raisePriority(vocabToQuiz);
		}
	}

	private boolean answeredCorrectly(String response, String correctAnswer) {
		return response.trim().equalsIgnoreCase(correctAnswer);
	}

	private boolean shouldQuizOnEnglishAnswer() {
		return languageChooser.nextInt(2) % 2 == 0;
	}

	private void printGoodbyeMessage() {
		System.out.println("Thanks for playing!");
	}
}
