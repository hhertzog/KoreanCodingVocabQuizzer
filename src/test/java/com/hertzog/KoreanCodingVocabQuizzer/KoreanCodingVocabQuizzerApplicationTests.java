package com.hertzog.KoreanCodingVocabQuizzer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

@SpringBootTest
class KoreanCodingVocabQuizzerApplicationTests {
	private static final String RESPONSES_FILE = System.getProperty("user.dir") +
			"\\src\\test\\java\\com\\hertzog\\KoreanCodingVocabQuizzer\\quizResponses";
	@Test
	void contextLoads() {
	}

	@Test
	public void whenRunMain_givenResponsesAsInput_thenRunsQuiz() throws IOException {
		String[] args = new String[0];
		final InputStream original = System.in;
		final FileInputStream fips = new FileInputStream(new File(RESPONSES_FILE));
		System.setIn(fips);

		KoreanCodingVocabQuizzerApplication.main(args);

		System.setIn(original);
	}
}
