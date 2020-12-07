package main;

import Common.ClientPromise;
import Exceptions.BadQuestionExceptions;
import main.Question;
import main.SessionMakerImplementation;
import main.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void testAnswerQuestion(){
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
