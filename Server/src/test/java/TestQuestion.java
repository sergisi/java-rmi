
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestion {

    private Question question;
    private String question_text = "Hola Que Tal";

    @BeforeAll
    void setUp(){
        question = new Question(question_text, 3);
    }

    @Test
    void testIsCorrectAnswer(){
        assertFalse(question.isCorrectAnswer(2));
        assertTrue(question.isCorrectAnswer(3));
    }

    @Test
    void testGetQuestion(){
        assertEquals(question.getQuestion(), question_text);
    }

    @Test
    void testPaseLine(){

    }

}
