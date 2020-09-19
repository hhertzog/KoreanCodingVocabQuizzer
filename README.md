# KoreanCodingVocabQuizzer

## Overview
Hello!
This is a program that quizzes the user on Korean vocabulary relating to software programming. It also allows the user to add new vocabulary to be quizzed on.
The quiz takes place in the console. Given a Korean word, answer with the English translation; given an English word, answer with the Korean translation. 
The quizzer goes between testing on Korean and English randomly.
The vocabularies are kept in a MongoDB collection, and each has a "priority." The priority of a vocabulary increases the more often its translation is incorrectly guessed,
and decreases the more often its translation is guessed correctly. The maximum priority level is currently 3 and the minimum is 1. Vocabulary priorities persist through
quizzing sessions, so that a vocabulary often missed in one session will still be high priority for the next session.
For now, all IP addresses are allowed to access the MongoDB vocabulary collection; this may change later if I choose to turn this project private.

I began this project with the purpose of learning the vocabulary I'll use while working as a software engineer in Korea. Knowing Korean programming lingo also helps me with
reading Korean coding blogs and books more easily. I also started this to learn more about the Spring framework and its capabilities via making a Spring app by myself,
as well as to set up and use a MongoDB database for the first time. 
Feel free to help by adding new coding-related vocabulary :)
For learning Korean vocabulary not related to programming, I recommend duolingo.com or memrise.com.


