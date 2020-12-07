package main;

import Common.ClientPromise;
import Exceptions.BadQuestionExceptions;
import main.Question;
import main.SessionMakerImplementation;
import main.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSessionMakerImplementation {

    SessionMakerImplementation newSession;
    Question q1Mock;
    Question q2Mock;

    @BeforeEach
    public void setUp() throws BadQuestionExceptions {
        q1Mock = mock(Question.class);
        q2Mock = mock(Question.class);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(q1Mock);
        questions.add(q2Mock);
        newSession = new SessionMakerImplementation(questions);
    }

    @Test
    public void testNewSession() {
        String idStudent = "hola";
        ClientPromise client = mock(ClientPromise.class);
        newSession.newSession(idStudent,client);
        HashMap<String, ClientPromise> clients = newSession.getClients();
        HashMap<String, UserSession> users = newSession.getUsers();
        assertTrue(clients.containsKey(idStudent) && users.containsKey(idStudent));
    }



    @Test
    public void testNewSessionMultipleUsers(){
        String idStudent = "hola";
        ClientPromise client = mock(ClientPromise.class);
        newSession.newSession(idStudent,client);
        HashMap<String, ClientPromise> clients = newSession.getClients();
        HashMap<String, UserSession> users = newSession.getUsers();
        assertTrue(clients.containsKey(idStudent) && users.containsKey(idStudent));
        String idStudent2 = "hola2";
        ClientPromise client2 = mock(ClientPromise.class);
        newSession.newSession(idStudent2,client2);
        clients = newSession.getClients();
        users = newSession.getUsers();
        assertTrue(clients.containsKey(idStudent2) && users.containsKey(idStudent2));
    }

    @Test
    public void testNewSessionSameUserTwoTimes(){
        String idStudent = "hola";
        ClientPromise client = mock(ClientPromise.class);
        newSession.newSession(idStudent,client);
        newSession.newSession(idStudent,client);
        HashMap<String, ClientPromise> clients = newSession.getClients();
        HashMap<String, UserSession> users = newSession.getUsers();
        assertTrue(clients.containsKey(idStudent) && users.containsKey(idStudent));
    }

    @Test
    public void testAnswerQuestionWrong(){
        when(q1Mock.isCorrectAnswer(2)).thenReturn(false);
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        UserSession  userMock = mock(UserSession.class);
        when(userMock.nextQuestion()).thenReturn(new UserSession(0,1));
        when(userMock.getActualQuestion()).thenReturn(0);
        newSession.newSession(idStudent,clientMock);
        newSession.setUserSession(idStudent,userMock);
        newSession.answerQuestion(idStudent,2);
        HashMap<String, UserSession> users = newSession.getUsers();
        assertEquals(0,users.get(idStudent).getCorrectAnswers());
        assertEquals(1,users.get(idStudent).getActualQuestion());
        verify(q1Mock).isCorrectAnswer(2);
    }

    @Test
    public void testAnswerQuestionCorrect(){
        when(q1Mock.isCorrectAnswer(2)).thenReturn(true);
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        UserSession  userMock = mock(UserSession.class);
        when(userMock.nextQuestionCorrect()).thenReturn(new UserSession(1,1));
        when(userMock.getActualQuestion()).thenReturn(0);
        newSession.newSession(idStudent,clientMock);
        newSession.setUserSession(idStudent,userMock);
        newSession.answerQuestion(idStudent,2);
        HashMap<String, UserSession> users = newSession.getUsers();
        assertEquals(1,users.get(idStudent).getCorrectAnswers());
        assertEquals(1,users.get(idStudent).getActualQuestion());
        verify(q1Mock).isCorrectAnswer(2);
    }

    @Test
    public void hasNext(){
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        UserSession  userMock = mock(UserSession.class);
        when(userMock.getActualQuestion()).thenReturn(0);
        newSession.newSession(idStudent,clientMock);
        newSession.setUserSession(idStudent,userMock);
        assertTrue(newSession.hasNext(idStudent));
        verify(userMock).getActualQuestion();
    }

    @Test
    public void hasntNext(){
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        UserSession  userMock = mock(UserSession.class);
        when(userMock.getActualQuestion()).thenReturn(2);
        newSession.newSession(idStudent,clientMock);
        newSession.setUserSession(idStudent,userMock);
        assertFalse(newSession.hasNext(idStudent));
        verify(userMock).getActualQuestion();
    }

    @Test
    public void testNext(){
        when(q1Mock.getQuestion()).thenReturn("" +
                "Hola Que tal?\n" +
                "- [1]: Tot Bé\n" +
                "- [2]: Reguling Regulang\n");
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        UserSession  userMock = mock(UserSession.class);
        when(userMock.getActualQuestion()).thenReturn(0);
        newSession.newSession(idStudent,clientMock);
        newSession.setUserSession(idStudent,userMock);
        String question = newSession.next(idStudent);
        assertEquals(question,q1Mock.getQuestion());
    }

    @Test
    public void testFinishExam(){
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        newSession.newSession(idStudent,clientMock);
        String idStudent2 = "hola2";
        ClientPromise clientMock2 = mock(ClientPromise.class);
        newSession.newSession(idStudent,clientMock);
        newSession.finishExam();
        verify(clientMock).finishExam(0,2);
        verify(clientMock).finishExam(0,2);
    }

    @Test
    public void testStartExam(){
        String idStudent = "hola";
        ClientPromise clientMock = mock(ClientPromise.class);
        newSession.newSession(idStudent,clientMock);
        String idStudent2 = "hola2";
        ClientPromise clientMock2 = mock(ClientPromise.class);
        newSession.newSession(idStudent,clientMock);
        newSession.startExam();
        verify(clientMock).startExam();
        verify(clientMock).startExam();
    }

}
