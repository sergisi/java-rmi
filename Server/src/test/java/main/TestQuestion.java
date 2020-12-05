package main;

import Exceptions.BadQuestionExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestion {

    private Question question;
    private final String questionTextNoChoice = "Hola Que tal?;1";
    private final String questionTextOneChoice = "Hola Que tal?;Estoy a tope jefe d'equipo;1";
    private final String questionTextTwoChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;1";
    private final String questionTextThreeChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;1";
    private final String questionTextFourChoices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;Estoy a tope jefe d'equipo;1";
    private final Integer correctAnswer = 1;
    private final String resultParseQuestionTwoChoices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [2]: Reguling Regulang\n";
    private final String resultParseQuestionThreeChoices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [2]: Reguling Regulang\n" +
            "- [3]: Es podria estar millor\n";
    private final String resultParseQuestionFourChoices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [2]: Reguling Regulang\n" +
            "- [3]: Es podria estar millor\n" +
            "- [4]: Estoy a tope jefe d'equipo\n";

    @Test
    void testIsCorrectAnswer() {
        question = new Question("Hola Que tal?;Tot Bé;Reguling Regulang", 1);
        assertFalse(question.isCorrectAnswer(2));
        assertTrue(question.isCorrectAnswer(1));
    }

    @Test
    void testGetQuestion() {
        question = new Question("Hola Que tal?;Tot Bé;Reguling Regulang", 1);
        assertEquals(question.getQuestion(), "Hola Que tal?;Tot Bé;Reguling Regulang");
    }

    @Test
    void testParseLine() throws BadQuestionExceptions {
        assertThrows(BadQuestionExceptions.class, () -> Question.parseLine(questionTextNoChoice));
        assertThrows(BadQuestionExceptions.class, () -> Question.parseLine(questionTextOneChoice));
        Question question1 = new Question(resultParseQuestionTwoChoices, correctAnswer);
        Question question2 = new Question(resultParseQuestionThreeChoices, correctAnswer);
        Question question3 = new Question(resultParseQuestionFourChoices, correctAnswer);
        assertEquals(question1, Question.parseLine(questionTextTwoChoices));
        assertEquals(question2, Question.parseLine(questionTextThreeChoices));
        assertEquals(question3, Question.parseLine(questionTextFourChoices));
    }

    @Test
    void testQuestion() throws BadQuestionExceptions {
        assertThrows(BadQuestionExceptions.class, () -> Question.parseLine("Hola que tal?"));
    }

}
