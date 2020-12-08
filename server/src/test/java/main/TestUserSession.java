package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserSession {

    @Test
    void testCreationIsZero(){
        UserSession userSession = new UserSession();
        assertEquals(userSession.getActualQuestion(),0);
        assertEquals(userSession.getCorrectAnswers(),0);
    }

    @Test
    void testNextQuestionCorrect1(){
        UserSession userSession1 = new UserSession();
        UserSession userSession2 = new UserSession(1,1);
        UserSession userSession3 = userSession1.nextQuestionCorrect();
        assertEquals(userSession2, userSession3);
    }

    @Test
    void testNextQuestionCorrect2(){
        UserSession userSession1 = new UserSession(9,15);
        UserSession userSession2 = new UserSession(10,16);
        UserSession userSession3 = userSession1.nextQuestionCorrect();
        assertEquals(userSession2, userSession3);
    }

    @Test
    void testNextQuestion1(){
        UserSession userSession1 = new UserSession();
        UserSession userSession2 = new UserSession(0,1);
        UserSession userSession3 = userSession1.nextQuestion();
        assertEquals(userSession2, userSession3);
    }

    @Test
    void testNextQuestion2(){
        UserSession userSession1 = new UserSession(13, 18);
        UserSession userSession2 = new UserSession(13,19);
        UserSession userSession3 = userSession1.nextQuestion();
        assertEquals(userSession2, userSession3);
    }
}
