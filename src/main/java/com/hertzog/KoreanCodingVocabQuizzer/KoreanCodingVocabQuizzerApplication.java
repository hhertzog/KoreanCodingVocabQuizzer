package com.hertzog.KoreanCodingVocabQuizzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class KoreanCodingVocabQuizzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoreanCodingVocabQuizzerApplication.class, args);
	}

}
