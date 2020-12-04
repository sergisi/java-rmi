
import Exceptions.BadQuestionExceptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestion {

    private Question question;
    private String question_text_no_choice = "Hola Que tal?;1";
    private String question_text_one_choice = "Hola Que tal?;Estoy a tope jefe d'equipo;1";
    private String question_text_two_choices = "Hola Que tal?;Tot Bé;Reguling Regulang;1";
    private String question_text_three_choices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;1";
    private String question_text_four_choices = "Hola Que tal?;Tot Bé;Reguling Regulang;Es podria estar millor;Estoy a tope jefe d'equipo;1";
    private Integer correctAnswer = 1;
    private String result_parse_question_two_choices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [1]: Reguling Regulang\n";
    private String result_parse_question_three_choices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [2]: Reguling Regulang\n" +
            "- [3]: Es podria estar millor\n";
    private String result_parse_question_four_choices = "" +
            "Hola Que tal?\n" +
            "- [1]: Tot Bé\n" +
            "- [2]: Reguling Regulang\n" +
            "- [3]: Es podria estar millor\n" +
            "- [4]: Es podria estar millor\n";

    @Test
    void testIsCorrectAnswer(){
        question = new Question("Hola Que tal?;Tot Bé;Reguling Regulang", 1);
        assertFalse(question.isCorrectAnswer(2));
        assertTrue(question.isCorrectAnswer(1));
    }

    @Test
    void testGetQuestion(){
        question = new Question("Hola Que tal?;Tot Bé;Reguling Regulang", 1);
        assertEquals(question.getQuestion(), "Hola Que tal?;Tot Bé;Reguling Regulang");
    }

    @Test
    void testPaseLine(){
        assertThrows(BadQuestionExceptions.class, () -> Question.parseLine(question_text_no_choice));
        assertThrows(BadQuestionExceptions.class, () -> Question.parseLine(question_text_one_choice));
        Question question1 = new Question(result_parse_question_two_choices, correctAnswer);
        Question question2 = new Question(result_parse_question_three_choices, correctAnswer);
        Question question3 = new Question(result_parse_question_four_choices, correctAnswer);
        try{
            assertEquals(question1, Question.parseLine(question_text_two_choices));
        }catch(Exception e){}
        try{
            assertEquals(question2, Question.parseLine(question_text_three_choices));
        }catch(Exception e){}
        try{
            assertEquals(question3, Question.parseLine(question_text_four_choices));
        }catch(Exception e){}
    }

}
