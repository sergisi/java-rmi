import Common.ClientPromise;
import Exceptions.BadQuestionExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestSessionMakerImplementation {

    @BeforeEach
    public void setUp() throws BadQuestionExceptions {
        try {
            Question q1 = Question.parseLine("Hola com estàs?;Molt Be;Anar fent;Fa fred;3");
        }catch (BadQuestionExceptions e){
            throw e;
        }
        try {
            Question q2 = Question.parseLine("Hola com estàs?;Molt Be;Anar fent;Fa fred;3");
        }catch (BadQuestionExceptions e){
            throw e;
        }
        ArrayList<Question> questions = new ArrayList<>();
        SessionMakerImplementation newSession = new SessionMakerImplementation();
    }

    @Test
    public void testNewSession() {

    }

    @Test
    public void testAnswerQuestion(){

    }

    @Test
    public void hasNext(){

    }

    @Test
    public void testNext(){

    }

    @Test
    public void testFinishExam(){

    }

    @Test
    public void testStartExam(){

    }

}
