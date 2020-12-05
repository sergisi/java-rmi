package main;

import Common.ClientPromise;
import Exceptions.BadQuestionExceptions;
import main.Question;
import main.SessionMakerImplementation;
import main.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSessionMakerImplementation {

    SessionMakerImplementation newSession;

    @BeforeEach
    public void setUp() throws BadQuestionExceptions {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
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
        newSession.newSession(idStudent,client);
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
        String idStudent = "hola";
        ClientPromise client = mock(ClientPromise.class);
        newSession.newSession(idStudent,client);
        newSession.answerQuestion(idStudent,3);
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
